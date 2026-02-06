package com.example.OneWave.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReflectionRequest {
    private Long sessionId;
    private Long applicationId;
    private List<String> aiGeneratedKeywords;
    private String userSummary;
    private String userImprovement;
}