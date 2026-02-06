package com.example.OneWave.service;

import com.example.OneWave.domain.Application;
import com.example.OneWave.domain.ApplicationStage;
import com.example.OneWave.domain.User;
import com.example.OneWave.dto.ApplicationDetailResponse;
import com.example.OneWave.dto.ApplicationRequest;
import com.example.OneWave.dto.ApplicationResponse;
import com.example.OneWave.repository.ApplicationRepository;
import com.example.OneWave.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest request) {
        // 1. 유저 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + request.getUserId()));

        // 2. 부모(Application) 생성
        Application application = Application.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .interviewDate(request.getInterviewDate())
                .failedStage(request.getFailedStage())
                .simpleMemo(request.getSimpleMemo())
                .build();

        // 3. 자식(Stages) 생성 및 연결
        if (request.getStages() != null) {
            for (ApplicationRequest.StageDto stageDto : request.getStages()) {
                ApplicationStage stage = ApplicationStage.builder()
                        .stageName(stageDto.getStageName())
                        .stageOrder(stageDto.getStageOrder())
                        .build();

                application.addStage(stage); // 리스트 추가 + 부모 설정
            }
        }

        // 4. 저장 (Cascade로 인해 stage들도 자동 저장)
        Application savedApp = applicationRepository.save(application);

        return new ApplicationResponse(savedApp);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailResponse getApplicationDetail(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원 내역입니다. id=" + applicationId));

        return new ApplicationDetailResponse(application);
    }
}