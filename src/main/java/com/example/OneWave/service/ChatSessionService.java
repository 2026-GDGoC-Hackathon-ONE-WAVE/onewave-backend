package com.example.OneWave.service;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.ChatMessage;
import com.example.OneWave.domain.ChatSession;
import com.example.OneWave.domain.User;
import com.example.OneWave.dto.ChatSessionStartRequest;
import com.example.OneWave.dto.ChatSessionStartResponse;
import com.example.OneWave.repository.ApplicationRepository;
import com.example.OneWave.repository.ChatMessageRepository;
import com.example.OneWave.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationRepository applicationRepository;
    private final PromptService promptService;

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
}
