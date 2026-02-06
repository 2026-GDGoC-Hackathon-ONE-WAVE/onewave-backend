package com.example.OneWave.service;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.ChatMessage;
import com.example.OneWave.domain.ChatSession;
import com.example.OneWave.domain.User;
import com.example.OneWave.dto.ChatHistoryResponse; // 추가됨
import com.example.OneWave.dto.ChatSessionStartRequest;
import com.example.OneWave.dto.ChatSessionStartResponse;
import com.example.OneWave.repository.ApplicationRepository;
import com.example.OneWave.repository.ChatMessageRepository;
import com.example.OneWave.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // 추가됨

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationRepository applicationRepository;
    private final PromptService promptService;

    /**
     * 챗봇 세션 시작
     */
    @Transactional
    public ChatSessionStartResponse startChatSession(ChatSessionStartRequest request) {
        // 1. Application 조회 (User 포함)
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원 내역을 찾을 수 없습니다."));

        User user = application.getUser();

        // 2. ChatSession 생성 및 저장
        ChatSession chatSession = ChatSession.builder()
                .application(application)
                .selectedEmotion(request.getSelectedEmotion())
                .build();
        ChatSession savedSession = chatSessionRepository.save(chatSession);

        // 3. 첫 번째 AI 메시지: 인사말 (고정)
        String greetingMessage = "안녕하세요! 회고를 시작해볼까요? 먼저 지금 기분을 골라주세요.";
        
        ChatMessage greetingChatMessage = ChatMessage.builder()
                .chatSession(savedSession)
                .senderType("AI")
                .messageContent(greetingMessage)
                .build();
        ChatMessage savedGreeting = chatMessageRepository.save(greetingChatMessage);

        // 4. 두 번째 AI 메시지: 감정 기반 개인화 메시지
        String emotionBasedMessage = promptService.buildFirstMessage(user, application, request.getSelectedEmotion());

        ChatMessage emotionBasedChatMessage = ChatMessage.builder()
                .chatSession(savedSession)
                .senderType("AI")
                .messageContent(emotionBasedMessage)
                .build();
        ChatMessage savedEmotionMessage = chatMessageRepository.save(emotionBasedChatMessage);

        // 5. Response 생성 (두 메시지 모두 포함)
        return ChatSessionStartResponse.builder()
                .sessionId(savedSession.getSessionId())
                .applicationId(application.getApplicationId())
                .companyName(application.getCompanyName())
                .jobTitle(application.getJobTitle())
                .selectedEmotion(savedSession.getSelectedEmotion())
                .createdAt(savedSession.getCreatedAt())
                .messages(List.of(
                        ChatSessionStartResponse.MessageDto.builder()
                                .messageId(savedGreeting.getMessageId())
                                .senderType(savedGreeting.getSenderType())
                                .content(savedGreeting.getMessageContent())
                                .createdAt(savedGreeting.getCreatedAt())
                                .build(),
                        ChatSessionStartResponse.MessageDto.builder()
                                .messageId(savedEmotionMessage.getMessageId())
                                .senderType(savedEmotionMessage.getSenderType())
                                .content(savedEmotionMessage.getMessageContent())
                                .createdAt(savedEmotionMessage.getCreatedAt())
                                .build()
                ))
                .build();
    }

    /**
     * 대화 내역 상세 조회
     */
    public ChatHistoryResponse getChatHistory(Long sessionId) {
        // 1. 세션 조회
        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        // 2. 메시지 목록 조회 (생성일 오름차순)
        List<ChatMessage> messages = chatMessageRepository.findByChatSessionOrderByCreatedAtAsc(chatSession);

        // 3. DTO 변환 및 반환
        return ChatHistoryResponse.from(chatSession, messages);
    }
}