package com.example.OneWave.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ApplicationRequest {
    private Long userId;
    private String companyName;
    private String jobTitle;
    private LocalDate interviewDate; // JSON "2025-02-01" 자동 변환
    private String failedStage;
    private String simpleMemo;
    private List<StageDto> stages; // 아래 내부 클래스 리스트

    @Getter
    @NoArgsConstructor
    public static class StageDto {
        private String stageName;
        private Integer stageOrder;
    }
}