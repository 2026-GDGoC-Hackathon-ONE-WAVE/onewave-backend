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
    private String selectedEmotion;  // 한글명으로 변환될 필드
    private String userSummary;
    private String userImprovement;
    private String simpleMemo;
    private List<KeywordDto> keywords;
    private LocalDateTime createdAt;

    public static ReflectionResponse from(Reflection reflection) {
        // 1. 영어 Enum 이름을 한글명으로 변환 (예: CONFUSED -> 당황)
        String emotionKorean = reflection.getSelectedEmotion();
        try {
            // 👇 getDescription()으로 수정하여 에러를 해결했습니다.
            emotionKorean = EmotionType.valueOf(reflection.getSelectedEmotion()).getDescription();
        } catch (Exception e) {
            // 변환 실패 시 DB에 저장된 원래 값(영어) 유지
        }

        return ReflectionResponse.builder()
                .reflectionId(reflection.getReflectionId())
                .sessionId(reflection.getSessionId())
                .applicationId(reflection.getApplication().getApplicationId())
                .companyName(reflection.getApplication().getCompanyName()) // Application에서 가져옴
                .jobTitle(reflection.getApplication().getJobTitle())       // Application에서 가져옴
                .selectedEmotion(emotionKorean)                          // 한글명 적용
                .userSummary(reflection.getUserSummary())
                .userImprovement(reflection.getUserImprovement())
                .simpleMemo(reflection.getApplication().getSimpleMemo())   // Application에서 가져옴
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