package com.example.OneWave.dto;

import com.example.OneWave.domain.Reflection;
import com.example.OneWave.domain.ReflectionKeyword;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReflectionResponse {
    private Long reflectionId;
    private Long sessionId;
    private Long applicationId;
    private String userSummary;
    private String userImprovement;
    private String selectedEmotion;
    private String simpleMemo;
    private Boolean isCompleted;
    private List<KeywordDto> keywords;
    private LocalDateTime createdAt;

    public static ReflectionResponse from(Reflection reflection) {
        return ReflectionResponse.builder()
                .reflectionId(reflection.getReflectionId())
                .sessionId(reflection.getSessionId())
                .applicationId(reflection.getApplication().getApplicationId())
                .userSummary(reflection.getUserSummary())
                .userImprovement(reflection.getUserImprovement())
                .selectedEmotion(reflection.getSelectedEmotion())
                .simpleMemo(reflection.getApplication().getSimpleMemo())
                .isCompleted(reflection.getIsCompleted())
                .keywords(reflection.getKeywords().stream()
                        .map(KeywordDto::from)
                        .collect(Collectors.toList()))
                .createdAt(reflection.getCreatedAt())
                .build();
    }

    @Getter
    @Builder
    public static class KeywordDto {
        private Long keywordId;
        private String keyword;
        private boolean isSelected;

        public static KeywordDto from(ReflectionKeyword entity) {
            return KeywordDto.builder()
                    .keywordId(entity.getKeywordId())
                    .keyword(entity.getKeyword())
                    .isSelected(entity.isSelected())
                    .build();
        }
    }
}