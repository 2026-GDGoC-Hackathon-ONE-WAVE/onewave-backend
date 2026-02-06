package com.example.OneWave.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ReflectionStatus {
    BEFORE("전"),
    IN_PROGRESS("진행중"),
    DONE("완료");

    private final String description;

    @JsonValue // 응답 나갈 때 "전"으로 나감
    public String getDescription() {
        return description;
    }

    @JsonCreator // 요청 들어올 때 "전" -> BEFORE 변환
    public static ReflectionStatus from(String value) {
        return Arrays.stream(ReflectionStatus.values())
                .filter(status -> status.getDescription().equals(value))
                .findFirst()
                .orElse(null);
    }
}