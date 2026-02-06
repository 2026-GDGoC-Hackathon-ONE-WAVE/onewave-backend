package com.example.OneWave.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class DashboardResponse {
    private Long userId;
    private SummaryDto summary;
    private List<StageFailureRateDto> stageFailureRates;
    private List<TopKeywordDto> topKeywords;
    private List<MonthlyCountDto> monthlyReflectionCount;

    @Getter
    @Builder
    public static class SummaryDto {
        private long totalReflections;
        private long thisMonthReflections;
    }

    @Getter
    @Builder
    public static class StageFailureRateDto {
        private String stage;
        private long failureCount;
        private long totalCount;
        private int failureRate;
        private String description;
    }

    @Getter
    @Builder
    public static class TopKeywordDto {
        private String keyword;
        private long count;
    }

    @Getter
    @Builder
    public static class MonthlyCountDto {
        private String month;
        private long count;
    }
}