package com.example.OneWave.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reflection_keyword")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReflectionKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reflection_id")
    private Reflection reflection;

    @Column(nullable = false)
    private String keyword;

    @Column(name = "is_selected")
    private boolean isSelected;

    @Builder
    public ReflectionKeyword(String keyword, boolean isSelected) {
        this.keyword = keyword;
        this.isSelected = isSelected;
    }

    public void setReflection(Reflection reflection) {
        this.reflection = reflection;
    }
}