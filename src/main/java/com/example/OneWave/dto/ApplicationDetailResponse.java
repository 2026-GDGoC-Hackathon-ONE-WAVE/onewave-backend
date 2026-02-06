package com.example.OneWave.dto;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.ApplicationStage;
import com.example.OneWave.domain.enums.ReflectionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ApplicationDetailResponse {
    private Long applicationId;
    private Long userId;
    private String companyName;
    private String jobTitle;
    private LocalDate interviewDate;
    private String failedStage;
    private String simpleMemo;
    private String reflectionStatus;
    private List<StageDto> stages;
    private String selectedEmotion;
    private List<String> selectedKeywords;

    public ApplicationDetailResponse(Application entity) {
        this.applicationId = entity.getApplicationId();
        this.userId = entity.getUser().getUserId();
        this.companyName = entity.getCompanyName();
        this.jobTitle = entity.getJobTitle();
        this.interviewDate = entity.getInterviewDate();
        this.failedStage = entity.getFailedStage();
        this.simpleMemo = entity.getSimpleMemo();
        
        // ReflectionStatus의 @JsonValue로 인해 자동으로 "전", "진행중", "완료"로 변환됨
        this.reflectionStatus = entity.getReflectionStatus() != null 
            ? entity.getReflectionStatus().getDescription()
            : "전";

        // stages 변환
        this.stages = entity.getStages().stream()
                .map(StageDto::new)
                .collect(Collectors.toList());

        // 감정 및 키워드
        this.selectedEmotion = entity.getSelectedEmotion();
        this.selectedKeywords = entity.getSelectedKeywords() != null && !entity.getSelectedKeywords().isEmpty()
                ? Arrays.asList(entity.getSelectedKeywords().split(","))
                : List.of();
    }

    @Getter
    public static class StageDto {
        private Long stageId;
        private String stageName;
        private Integer stageOrder;

        public StageDto(ApplicationStage stage) {
            this.stageId = stage.getStageId();
            this.stageName = stage.getStageName();
            this.stageOrder = stage.getStageOrder();
        }
    }
}
