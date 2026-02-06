package com.example.OneWave.dto;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.ApplicationStage;
import com.example.OneWave.domain.enums.ReflectionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ApplicationResponse {
    private Long applicationId;
    private Long userId;
    private String companyName;
    private String jobTitle;
    private LocalDate interviewDate;
    private String failedStage;
    private String simpleMemo;
    private ReflectionStatus reflectionStatus;
    private List<StageResponse> stages;
    private LocalDateTime createdAt;

    public ApplicationResponse(Application entity) {
        this.applicationId = entity.getApplicationId();
        this.userId = entity.getUser().getUserId();
        this.companyName = entity.getCompanyName();
        this.jobTitle = entity.getJobTitle();
        this.interviewDate = entity.getInterviewDate();
        this.failedStage = entity.getFailedStage();
        this.simpleMemo = entity.getSimpleMemo();
        this.reflectionStatus = entity.getReflectionStatus();
        this.createdAt = entity.getCreatedAt();

        // Entity List -> DTO List 변환
        this.stages = entity.getStages().stream()
                .map(StageResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class StageResponse {
        private Long stageId;
        private String stageName;
        private Integer stageOrder;

        public StageResponse(ApplicationStage stage) {
            this.stageId = stage.getStageId();
            this.stageName = stage.getStageName();
            this.stageOrder = stage.getStageOrder();
        }
    }
}