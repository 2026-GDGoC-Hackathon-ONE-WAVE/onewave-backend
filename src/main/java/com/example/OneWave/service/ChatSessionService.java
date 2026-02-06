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

        // 3. 첫 AI 메시지 생성 (프롬프트 기반)
        String firstMessageContent = promptService.buildFirstMessage(user, application, request.getSelectedEmotion());

        ChatMessage firstMessage = ChatMessage.builder()
                .chatSession(savedSession)
                .senderType("AI")
                .messageContent(firstMessageContent)
                .build();
        ChatMessage savedMessage = chatMessageRepository.save(firstMessage);

        // 4. Response 생성
        return ChatSessionStartResponse.builder()
                .sessionId(savedSession.getSessionId())
                .applicationId(application.getApplicationId())
                .companyName(application.getCompanyName())
                .jobTitle(application.getJobTitle())
                .selectedEmotion(savedSession.getSelectedEmotion())
                .createdAt(savedSession.getCreatedAt())
                .firstMessage(ChatSessionStartResponse.FirstMessageDto.builder()
                        .messageId(savedMessage.getMessageId())
                        .senderType(savedMessage.getSenderType())
                        .content(savedMessage.getMessageContent())
                        .createdAt(savedMessage.getCreatedAt())
                        .build())
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