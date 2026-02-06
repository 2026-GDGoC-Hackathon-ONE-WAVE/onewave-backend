package com.example.OneWave.controller;

import com.example.OneWave.dto.ApplicationListResponse;
import com.example.OneWave.dto.ApplicationRequest;
import com.example.OneWave.dto.ApplicationResponse;
import com.example.OneWave.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // 1. 등록 (POST)
    @PostMapping
    public ResponseEntity<Map<String, Object>> createApplication(@RequestBody ApplicationRequest request) {
        ApplicationResponse savedApplication = applicationService.createApplication(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 201);
        response.put("success", true);
        response.put("message", "경험 내역이 성공적으로 등록되었습니다.");
        response.put("data", savedApplication);

        return ResponseEntity.ok(response);
    }

    // 목록 조회 (GET) 추가
    @GetMapping
    public ResponseEntity<Map<String, Object>> getApplications(@RequestParam Long userId) {
        ApplicationListResponse applicationList = applicationService.getApplications(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("success", true);
        response.put("message", "지원 내역 목록 조회 성공");
        response.put("data", applicationList);

        return ResponseEntity.ok(response);
    }
}