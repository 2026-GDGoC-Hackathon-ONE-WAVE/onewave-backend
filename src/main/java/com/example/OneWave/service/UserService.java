package com.example.OneWave.service;

import com.example.OneWave.domain.User;
import com.example.OneWave.dto.UserOnboardingRequest;
import com.example.OneWave.dto.UserResponseDto;
import com.example.OneWave.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto registerUser(Long userId, UserOnboardingRequest request) {
        // 1. Preparation Method List -> String 변환
        String preparationMethodStr = "";
        if (request.getPreparationMethod() != null && !request.getPreparationMethod().isEmpty()) {
            preparationMethodStr = String.join(",", request.getPreparationMethod());
        }

        // 2. 사용자 조회 (없으면 예외 발생 혹은 새로 생성 정책 결정 필요)
        // 여기서는 "기존에 가입된 유저의 정보를 업데이트한다"고 가정합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. userId=" + userId));

        // 3. 정보 업데이트 (Dirty Checking)
        user.updateOnboardingInfo(
                request.getName(),
                request.getJobCategory(),
                request.getCareerStage(),
                preparationMethodStr
        );

        // 4. 변경된 정보를 DTO로 반환
        return new UserResponseDto(user);
    }
}