package com.example.OneWave.controller;

import com.example.OneWave.dto.*;
import com.example.OneWave.service.ChatMessageService;
import com.example.OneWave.service.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat-sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService chatSessionService;
    private final ChatMessageService chatMessageService;

    /**
     * 챗봇 세션 시작 API
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<ChatSessionStartResponse>> startChatSession(
            @RequestBody ChatSessionStartRequest request) {
        
        ChatSessionStartResponse response = chatSessionService.startChatSession(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        201,
                        "챗봇 세션이 성공적으로 시작되었습니다.",
                        response
                ));
    }

    /**
     * 챗봇 대화 계속하기 API (Gemini 연동)
     * POST /api/chat-sessions/{sessionId}/messages
     */
    @PostMapping("/{sessionId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> sendMessage(
            @PathVariable Long sessionId,
            @RequestBody ChatMessageRequest request) {
        
        ChatMessageResponse response = chatMessageService.sendMessage(sessionId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        201,
                        "메시지가 성공적으로 전송되었습니다.",
                        response
                )
        );
    }
}
