package com.example.OneWave.domain;

import com.example.OneWave.domain.enums.ReflectionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String jobTitle;
    private LocalDate interviewDate;
    private String failedStage;

    @Column(columnDefinition = "TEXT")
    private String simpleMemo;

    @Enumerated(EnumType.STRING)
    private ReflectionStatus reflectionStatus;

    // 1:N 관계 (기존 코드)
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationStage> stages = new ArrayList<>();

    // 👇 [추가] 1:1 관계 (DashboardService의 app.getReflection() 에러 해결)
    @OneToOne(mappedBy = "application")
    private Reflection reflection;

    private String selectedEmotion;

    @Column(columnDefinition = "TEXT")
    private String selectedKeywords;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Application(User user, String companyName, String jobTitle, LocalDate interviewDate,
                       String failedStage, String simpleMemo) {
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.interviewDate = interviewDate;
        this.failedStage = failedStage;
        this.simpleMemo = simpleMemo;
        this.reflectionStatus = ReflectionStatus.BEFORE;
    }

    public void addStage(ApplicationStage stage) {
        this.stages.add(stage);
        stage.setApplication(this);
    }

    public void updateEmotionAndKeywords(String emotion, List<String> keywords) {
        this.selectedEmotion = emotion;
        this.selectedKeywords = String.join(",", keywords);
    }

    public void updateReflectionStatus(ReflectionStatus status) {
        this.reflectionStatus = status;
    }
}