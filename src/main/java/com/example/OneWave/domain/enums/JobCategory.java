package com.example.OneWave.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum JobCategory {
    DEV("개발"),
    DESIGN("디자인"),
    PM("기획/PM"),
    MARKETING("마케팅");

    private final String description;

    // Response 나갈 때: "DEV" -> "개발"
    @JsonValue
    public String getDescription() {
        return description;
    }

    // Request 들어올 때: "개발" -> "DEV"
    @JsonCreator
    public static JobCategory from(String value) {
        return Arrays.stream(JobCategory.values())
                .filter(category -> category.getDescription().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 직군입니다: " + value));
    }
}
