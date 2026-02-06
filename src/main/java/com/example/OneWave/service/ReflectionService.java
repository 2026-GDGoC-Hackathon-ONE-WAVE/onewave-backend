package com.example.OneWave.service;

import com.example.OneWave.domain.*;
import com.example.OneWave.domain.enums.ReflectionStatus;
import com.example.OneWave.dto.ReflectionRequest;
import com.example.OneWave.dto.ReflectionResponse;
import com.example.OneWave.repository.ApplicationRepository;
import com.example.OneWave.repository.ChatSessionRepository;
import com.example.OneWave.repository.ReflectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final ApplicationRepository applicationRepository;
    private final ChatSessionRepository chatSessionRepository;

    @Transactional
    public ReflectionResponse createReflection(ReflectionRequest request) {
        // 1. 지원 내역(Application) 조회
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원 내역을 찾을 수 없습니다."));

        // 2. 채팅 세션 조회
        ChatSession chatSession = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        // 3. 회고(Reflection) 엔티티 생성
        Reflection reflection = Reflection.builder()
                .application(application)
                .sessionId(request.getSessionId())
                .userSummary(request.getUserSummary())
                .userImprovement(request.getUserImprovement())
                .selectedEmotion(chatSession.getSelectedEmotion().name())
                .build();

        // 4. 키워드 저장 (Detail용)
        if (request.getAiGeneratedKeywords() != null) {
            for (String keywordStr : request.getAiGeneratedKeywords()) {
                ReflectionKeyword keyword = ReflectionKeyword.builder()
                        .keyword(keywordStr)
                        .isSelected(false)
                        .build();
                reflection.addKeyword(keyword);
            }
        }

        // 5. 회고 저장
        Reflection savedReflection = reflectionRepository.save(reflection);

        // (지원 내역 상태 & 키워드 업데이트)
        // 6-1. 상태를 'DONE(완료)'으로 변경
        application.updateReflectionStatus(ReflectionStatus.DONE);

        // 6-2. 목록 조회용 키워드 및 감정 업데이트
        if (request.getAiGeneratedKeywords() != null) {
            application.updateEmotionAndKeywords(
                    chatSession.getSelectedEmotion().name(),
                    request.getAiGeneratedKeywords()
            );
        }

        return ReflectionResponse.from(savedReflection);
    }
}