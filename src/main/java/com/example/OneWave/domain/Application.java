package com.example.OneWave.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", length = 100, nullable = false)
    private String companyName;

    @Column(name = "job_title", length = 100, nullable = false)
    private String jobTitle;

    @Column(name = "interview_date")
    private LocalDate interviewDate;

    @Column(name = "failed_stage", length = 50)
    private String failedStage;

    @Column(name = "simple_memo", columnDefinition = "TEXT")
    private String simpleMemo;

    @Column(name = "reflection_status", length = 20)
    private String reflectionStatus; // 전/중/완료

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Application(User user, String companyName, String jobTitle, LocalDate interviewDate,
                       String failedStage, String simpleMemo, String reflectionStatus) {
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.interviewDate = interviewDate;
        this.failedStage = failedStage;
        this.simpleMemo = simpleMemo;
        this.reflectionStatus = reflectionStatus;
    }
}
