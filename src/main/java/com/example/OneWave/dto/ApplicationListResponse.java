package com.example.OneWave.dto;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.enums.ReflectionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ApplicationListResponse {
    private List<ApplicationSummaryDto> applications;
    private int totalCount;

    public ApplicationListResponse(List<ApplicationSummaryDto> applications) {
        this.applications = applications;
        this.totalCount = applications.size();
    }

    @Getter
    public static class ApplicationSummaryDto {
        private Long applicationId;
        private String companyName;
        private String jobTitle;
        private LocalDate interviewDate;
        private String failedStage;
        private ReflectionStatus reflectionStatus;
        private List<String> selectedKeywords;

        public ApplicationSummaryDto(Application entity) {
            this.applicationId = entity.getApplicationId();
            this.companyName = entity.getCompanyName();
            this.jobTitle = entity.getJobTitle();
            this.interviewDate = entity.getInterviewDate();
            this.failedStage = entity.getFailedStage();
            this.reflectionStatus = entity.getReflectionStatus();

            // 추후 회고(Reflection) 기능 구현 시 실제 데이터로 교체 예정
            this.selectedKeywords = new ArrayList<>();
        }
    }
}