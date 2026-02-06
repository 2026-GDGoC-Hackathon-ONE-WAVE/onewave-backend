package com.example.OneWave.dto;

import com.example.OneWave.domain.enums.CareerStage;
import com.example.OneWave.domain.enums.JobCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserOnboardingRequest {
    private String name;
    private JobCategory jobCategory;
    private CareerStage careerStage;
    private List<String> preparationMethod;
}