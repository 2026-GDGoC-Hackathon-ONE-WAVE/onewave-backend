package com.example.OneWave.controller;

import com.example.OneWave.dto.ApiResponse;
import com.example.OneWave.dto.ReflectionCompleteResponse;
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

    /**
     * 회고 최종 완료 처리 API (isCompleted = true)
     * PATCH /api/reflections/{reflectionId}/complete
     */
    @PatchMapping("/{reflectionId}/complete")
    public ResponseEntity<ApiResponse<ReflectionCompleteResponse>> completeReflection(
            @PathVariable Long reflectionId) {

        ReflectionCompleteResponse response = reflectionService.completeReflection(reflectionId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "회고가 성공적으로 완료되었습니다.",
                        response
                )
        );
    }

    /**
     *  회고 상세 조회 API
     * GET /api/reflections/{reflectionId}
     */
    @GetMapping("/{reflectionId}")
    public ResponseEntity<ApiResponse<ReflectionResponse>> getReflection(@PathVariable Long reflectionId) {

        ReflectionResponse response = reflectionService.getReflection(reflectionId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "회고 상세 조회 성공",
                        response
                )
        );
    }
}