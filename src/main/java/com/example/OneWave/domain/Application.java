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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String jobTitle;
    private LocalDate interviewDate; // yyyy-MM-dd
    private String failedStage;

    @Column(columnDefinition = "TEXT")
    private String simpleMemo;

    @Enumerated(EnumType.STRING)
    private ReflectionStatus reflectionStatus; // "전"

    // 1:N 관계 (부모가 저장될 때 자식들도 같이 저장됨)
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationStage> stages = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Application(User user, String companyName, String jobTitle, LocalDate interviewDate, String failedStage, String simpleMemo) {
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.interviewDate = interviewDate;
        this.failedStage = failedStage;
        this.simpleMemo = simpleMemo;
        this.reflectionStatus = ReflectionStatus.BEFORE; // 기본값 설정
    }

    // 연관관계 편의 메서드 (자식 추가)
    public void addStage(ApplicationStage stage) {
        this.stages.add(stage);
        stage.setApplication(this);
    }
}