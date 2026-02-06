package com.example.OneWave.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EmotionType {
    CONFUSED("당황"),
    REGRETFUL("아쉬움"),
    ANGRY("분함"),
    SAD("슬픔"),
    DISAPPOINTED("실망"),
    FRUSTRATED("답답함");

    private final String description;

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static EmotionType from(String value) {
        return Arrays.stream(EmotionType.values())
                .filter(emotion -> emotion.getDescription().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 감정입니다: " + value));
    }
}
