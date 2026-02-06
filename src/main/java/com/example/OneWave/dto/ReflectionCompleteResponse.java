package com.example.OneWave.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReflectionCompleteResponse {
    private Long reflectionId;
    private Boolean isCompleted;
}