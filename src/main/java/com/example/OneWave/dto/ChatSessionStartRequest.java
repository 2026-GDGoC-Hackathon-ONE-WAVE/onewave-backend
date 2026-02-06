package com.example.OneWave.dto;

import com.example.OneWave.domain.enums.EmotionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatSessionStartRequest {
    private Long applicationId;
    private EmotionType selectedEmotion;
}
