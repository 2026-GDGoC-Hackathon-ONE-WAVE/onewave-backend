package com.example.OneWave.controller;

import com.example.OneWave.dto.ApiResponse;
import com.example.OneWave.dto.ReflectionRequest;
import com.example.OneWave.dto.ReflectionResponse;
import com.example.OneWave.service.ReflectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reflections")
@RequiredArgsConstructor
public class ReflectionController {

    private final ReflectionService reflectionService;

    /**
     * 회고 완료 및 저장 API
     * POST /api/reflections
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReflectionResponse>> createReflection(@RequestBody ReflectionRequest request) {

        ReflectionResponse response = reflectionService.createReflection(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        201,
                        "회고가 성공적으로 생성되었습니다.",
                        response
                ));
    }
}