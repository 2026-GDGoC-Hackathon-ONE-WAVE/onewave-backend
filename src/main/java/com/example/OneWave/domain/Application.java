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

    // 1:N 관계
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationStage> stages = new ArrayList<>();

    // 감정 및 키워드 (목록 조회용 요약 데이터)
    private String selectedEmotion;

    @Column(columnDefinition = "TEXT")
    private String selectedKeywords; // 예: "성장,몰입,도전" (쉼표로 구분 or JSON)

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
        this.reflectionStatus = ReflectionStatus.BEFORE; // 생성 시 기본값 '전'
    }

    // 연관관계 편의 메서드
    public void addStage(ApplicationStage stage) {
        this.stages.add(stage);
        stage.setApplication(this);
    }

    // 감정 및 키워드 설정 메서드
    public void updateEmotionAndKeywords(String emotion, List<String> keywords) {
        this.selectedEmotion = emotion;
        // 리스트를 콤마 문자열로 변환해서 저장 (목록 조회 성능 최적화)
        this.selectedKeywords = String.join(",", keywords);
    }

    // 👇 [필수 추가] 상태 변경 메서드 (ReflectionService에서 사용)
    public void updateReflectionStatus(ReflectionStatus status) {
        this.reflectionStatus = status;
    }
}