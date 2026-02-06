package com.example.OneWave.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reflection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reflection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reflection_id")
    private Long reflectionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "user_summary", columnDefinition = "TEXT")
    private String userSummary;

    @Column(name = "user_improvement", columnDefinition = "TEXT")
    private String userImprovement;

    @Column(name = "selected_emotion")
    private String selectedEmotion;

    // 👇 [추가] 완료 여부 필드 (기본값 false)
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @OneToMany(mappedBy = "reflection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReflectionKeyword> keywords = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Reflection(Application application, Long sessionId, String userSummary, String userImprovement, String selectedEmotion) {
        this.application = application;
        this.sessionId = sessionId;
        this.userSummary = userSummary;
        this.userImprovement = userImprovement;
        this.selectedEmotion = selectedEmotion;
        this.isCompleted = false; // 생성 시 기본 false
    }

    public void addKeyword(ReflectionKeyword keyword) {
        this.keywords.add(keyword);
        keyword.setReflection(this);
    }

    // 👇 [추가] 완료 처리 메서드
    public void complete() {
        this.isCompleted = true;
    }
}