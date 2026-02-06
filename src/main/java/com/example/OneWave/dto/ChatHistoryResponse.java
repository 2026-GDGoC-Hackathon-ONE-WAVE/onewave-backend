package com.example.OneWave.dto;

import com.example.OneWave.domain.ChatMessage;
import com.example.OneWave.domain.ChatSession;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ChatHistoryResponse {
    private Long sessionId;
    private Long applicationId;
    private String selectedEmotion;
    private Boolean isCompleted;
    private List<MessageDto> messages;

    // Entity -> DTO 변환 편의 메서드 (Builder 패턴 활용)
    public static ChatHistoryResponse from(ChatSession session, List<ChatMessage> messages) {
        return ChatHistoryResponse.builder()
                .sessionId(session.getSessionId())
                .applicationId(session.getApplication().getApplicationId())
                .selectedEmotion(session.getSelectedEmotion() != null ? session.getSelectedEmotion().name() : null)
                .isCompleted(session.getIsCompleted())
                .messages(messages.stream()
                        .map(MessageDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    public static class MessageDto {
        private Long messageId;
        private String senderType;
        private String content; // JSON에서는 content로 출력
        private LocalDateTime createdAt;

        public static MessageDto from(ChatMessage message) {
            return MessageDto.builder()
                    .messageId(message.getMessageId())
                    .senderType(message.getSenderType())
                    .content(message.getMessageContent()) // DB 필드명 매핑
                    .createdAt(message.getCreatedAt())
                    .build();
        }
    }
}