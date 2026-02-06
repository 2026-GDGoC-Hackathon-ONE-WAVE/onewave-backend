package com.example.OneWave.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CareerStage {
    NEW("취준생/신입"),
    JUNIOR("주니어 (1~3년)"),
    MIDDLE("미들 (4~7년)"),
    SENIOR("시니어 (8년 이상)");

    private final String description;

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static CareerStage from(String value) {
        return Arrays.stream(CareerStage.values())
                .filter(stage -> stage.getDescription().equals(value))
                .findFirst()
                .orElse(null);
    }
}
