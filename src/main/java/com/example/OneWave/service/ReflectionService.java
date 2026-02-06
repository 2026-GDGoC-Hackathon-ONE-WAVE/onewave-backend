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

        // 2. 채팅 세션 조회 (감정 정보를 가져오기 위해)
        ChatSession chatSession = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        // 3. 회고(Reflection) 엔티티 생성
        Reflection reflection = Reflection.builder()
                .application(application)
                .sessionId(request.getSessionId())
                .userSummary(request.getUserSummary())
                .userImprovement(request.getUserImprovement())
                .selectedEmotion(chatSession.getSelectedEmotion().name()) // Enum -> String
                .build();

        // 4. 키워드 저장 (AI가 준 키워드는 기본적으로 isSelected = false)
        if (request.getAiGeneratedKeywords() != null) {
            for (String keywordStr : request.getAiGeneratedKeywords()) {
                ReflectionKeyword keyword = ReflectionKeyword.builder()
                        .keyword(keywordStr)
                        .isSelected(false)
                        .build();
                reflection.addKeyword(keyword); // 연관관계 편의 메서드 사용
            }
        }

        // 5. 저장
        Reflection savedReflection = reflectionRepository.save(reflection);

        // 6. [중요] Application 상태를 '회고 완료'로 변경
        // Application 엔티티에 updateReflectionStatus 메서드가 필요합니다. (아래 참고)
        // application.updateReflectionStatus(ReflectionStatus.DONE);
        // *Application.java에 해당 메서드가 없다면 추가해주세요.*

        return ReflectionResponse.from(savedReflection);
    }
}