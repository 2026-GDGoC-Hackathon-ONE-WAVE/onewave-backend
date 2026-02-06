package com.example.OneWave.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private MessageDto userMessage;
    private MessageDto aiMessage;

    @Getter
    @Builder
    public static class MessageDto {
        private Long messageId;
        private String senderType;
        private String content;
        private LocalDateTime createdAt;
    }
}
