package com.example.OneWave.domain;

import com.example.OneWave.domain.enums.CareerStage;
import com.example.OneWave.domain.enums.JobCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JobCategory jobCategory;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CareerStage careerStage;

    @Column(columnDefinition = "TEXT")
    private String preparationMethod;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public User(String name, JobCategory jobCategory, CareerStage careerStage, String preparationMethod) {
        this.name = name;
        this.jobCategory = jobCategory;
        this.careerStage = careerStage;
        this.preparationMethod = preparationMethod;
    }

    public void updateOnboardingInfo(String name, JobCategory jobCategory, CareerStage careerStage, String preparationMethod) {
        this.name = name;
        this.jobCategory = jobCategory;
        this.careerStage = careerStage;
        this.preparationMethod = preparationMethod;
    }
}