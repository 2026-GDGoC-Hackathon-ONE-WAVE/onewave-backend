package com.example.OneWave.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "application_stages")
public class ApplicationStage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    private String stageName;
    private Integer stageOrder;

    @Builder
    public ApplicationStage(String stageName, Integer stageOrder) {
        this.stageName = stageName;
        this.stageOrder = stageOrder;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}