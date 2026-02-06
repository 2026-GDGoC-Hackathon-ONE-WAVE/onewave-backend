package com.example.OneWave.controller;

import com.example.OneWave.dto.ApiResponse;
import com.example.OneWave.dto.DashboardResponse;
import com.example.OneWave.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardData(@RequestParam Long userId) {

        DashboardResponse response = dashboardService.getDashboardData(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "대시보드 데이터 조회 성공",
                        response
                )
        );
    }
}