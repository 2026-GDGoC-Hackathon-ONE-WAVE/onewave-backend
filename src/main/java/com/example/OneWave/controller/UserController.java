package com.example.OneWave.controller;

import com.example.OneWave.dto.UserOnboardingRequest;
import com.example.OneWave.dto.UserResponseDto;
import com.example.OneWave.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 온보딩 정보 등록/수정 API
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> registerUserInfo(
            @PathVariable Long userId,
            @RequestBody UserOnboardingRequest request
    ) {
        UserResponseDto savedUser = userService.registerUser(userId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("success", true);
        response.put("message", "사용자 정보가 성공적으로 저장되었습니다.");
        response.put("data", savedUser);

        return ResponseEntity.ok(response);
    }
}