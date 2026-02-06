package com.example.OneWave.dto;

import com.example.OneWave.domain.Reflection;
import com.example.OneWave.domain.ReflectionKeyword;
import com.example.OneWave.domain.enums.EmotionType;
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
    private String companyName;
    private String jobTitle;
    private String selectedEmotion;
    private String userSummary;
    private String userImprovement;
    private String simpleMemo;
    private List<KeywordDto> keywords;
    private LocalDateTime createdAt;

    public static ReflectionResponse from(Reflection reflection) {
        String emotionKorean = reflection.getSelectedEmotion();
        try {
            // ✅ getDescription()으로 수정하여 Enum 에러 해결
            emotionKorean = EmotionType.valueOf(reflection.getSelectedEmotion()).getDescription();
        } catch (Exception e) {
            // 변환 실패 시 DB에 저장된 원래 값 유지
        }

        return ReflectionResponse.builder()
                .reflectionId(reflection.getReflectionId())
                .sessionId(reflection.getSessionId())
                .applicationId(reflection.getApplication().getApplicationId())
                .companyName(reflection.getApplication().getCompanyName())
                .jobTitle(reflection.getApplication().getJobTitle())
                .selectedEmotion(emotionKorean)
                .userSummary(reflection.getUserSummary())
                .userImprovement(reflection.getUserImprovement())
                .simpleMemo(reflection.getApplication().getSimpleMemo())
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