package com.example.OneWave.service;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.ChatMessage;
import com.example.OneWave.domain.ChatSession;
import com.example.OneWave.domain.User;
import com.example.OneWave.dto.ChatMessageRequest;
import com.example.OneWave.dto.ChatMessageResponse;
import com.example.OneWave.repository.ChatMessageRepository;
import com.example.OneWave.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatMessageService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GeminiService geminiService;
    private final PromptService promptService;

    @Transactional
    public ChatMessageResponse sendMessage(Long sessionId, ChatMessageRequest request) {
        // 1. ChatSession 조회 (Application, User 포함)
        ChatSession chatSession = chatSessionRepository.findByIdWithApplicationAndUser(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        Application application = chatSession.getApplication();
        User user = application.getUser();

        // 2. 사용자 메시지 저장
        ChatMessage userMessage = ChatMessage.builder()
                .chatSession(chatSession)
                .senderType("USER")
                .messageContent(request.getMessage())
                .build();
        ChatMessage savedUserMessage = chatMessageRepository.save(userMessage);

        // 3. 대화 히스토리 구성
        List<ChatMessage> messageHistory = chatMessageRepository.findByChatSessionOrderByCreatedAtAsc(chatSession);
        String conversationHistory = buildConversationHistory(messageHistory);

        // 4. System Prompt 생성
        String systemPrompt = promptService.buildSystemPrompt(user, application);

        // 5. Gemini API 호출
        String aiResponse = geminiService.generateResponse(
                systemPrompt,
                conversationHistory
        );

        // 6. AI 응답 저장
        ChatMessage aiMessage = ChatMessage.builder()
                .chatSession(chatSession)
                .senderType("AI")
                .messageContent(aiResponse)
                .build();
        ChatMessage savedAiMessage = chatMessageRepository.save(aiMessage);

        // 7. Response 생성 (명세서 형식에 맞춤)
        return ChatMessageResponse.builder()
                .userMessage(ChatMessageResponse.MessageDto.builder()
                        .messageId(savedUserMessage.getMessageId())
                        .senderType(savedUserMessage.getSenderType())
                        .content(savedUserMessage.getMessageContent())
                        .createdAt(savedUserMessage.getCreatedAt())
                        .build())
                .aiMessage(ChatMessageResponse.MessageDto.builder()
                        .messageId(savedAiMessage.getMessageId())
                        .senderType(savedAiMessage.getSenderType())
                        .content(savedAiMessage.getMessageContent())
                        .createdAt(savedAiMessage.getCreatedAt())
                        .build())
                .build();
    }

    /**
     * 대화 히스토리를 문자열로 변환
     */
    private String buildConversationHistory(List<ChatMessage> messages) {
        return messages.stream()
                .map(msg -> String.format("[%s]: %s", msg.getSenderType(), msg.getMessageContent()))
                .collect(Collectors.joining("\n\n"));
    }
}
