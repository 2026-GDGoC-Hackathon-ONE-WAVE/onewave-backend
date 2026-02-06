package com.example.OneWave.dto;

import com.example.OneWave.domain.User;
import com.example.OneWave.domain.enums.CareerStage;
import com.example.OneWave.domain.enums.JobCategory;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
public class UserResponseDto {
    private Long userId;
    private String name;
    private JobCategory jobCategory;
    private CareerStage careerStage;
    private List<String> preparationMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.jobCategory = entity.getJobCategory();
        this.careerStage = entity.getCareerStage();

        // DB 문자열(콤마 구분) -> 리스트 변환
        if (entity.getPreparationMethod() != null && !entity.getPreparationMethod().isEmpty()) {
            this.preparationMethod = Arrays.asList(entity.getPreparationMethod().split(","));
        }

        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}