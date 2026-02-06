package com.example.OneWave.dto;

import com.example.OneWave.domain.enums.EmotionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatSessionStartResponse {
    private Long sessionId;
    private Long applicationId;
    private String companyName;
    private String jobTitle;
    private EmotionType selectedEmotion;
    private LocalDateTime createdAt;
    private List<MessageDto> messages;

    @Getter
    @Builder
    public static class MessageDto {
        private Long messageId;
        private String senderType;
        private String content;
        private LocalDateTime createdAt;
    }
}
