package com.example.OneWave.dto;

import com.example.OneWave.domain.enums.EmotionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatSessionStartResponse {
    private Long sessionId;
    private Long applicationId;
    private String companyName;
    private String jobTitle;
    private EmotionType selectedEmotion;
    private LocalDateTime createdAt;
    private FirstMessageDto firstMessage;

    @Getter
    @Builder
    public static class FirstMessageDto {
        private Long messageId;
        private String senderType;
        private String content;
        private LocalDateTime createdAt;
    }
}
