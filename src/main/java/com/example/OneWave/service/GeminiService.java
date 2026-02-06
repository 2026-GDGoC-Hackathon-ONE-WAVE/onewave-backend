package com.example.OneWave.service;

import com.example.OneWave.config.GeminiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final GeminiConfig geminiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";

    /**
     * Gemini API 호출하여 응답 생성
     */
    public String generateResponse(String systemPrompt, String userMessage) {
        try {
            String url = String.format(GEMINI_API_URL, geminiConfig.getModel(), geminiConfig.getApiKey());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = buildRequestBody(systemPrompt, userMessage);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            return extractTextFromResponse(response.getBody());

        } catch (Exception e) {
            log.error("Gemini API 호출 실패: {}", e.getMessage());
            return "죄송합니다. 일시적인 오류가 발생했습니다. 다시 시도해주세요.";
        }
    }

    /**
     * Gemini API 요청 바디 생성
     */
    private Map<String, Object> buildRequestBody(String systemPrompt, String userMessage) {
        Map<String, Object> requestBody = new HashMap<>();

        // System instruction 설정
        Map<String, Object> systemInstruction = new HashMap<>();
        Map<String, Object> systemParts = new HashMap<>();
        systemParts.put("text", systemPrompt);
        systemInstruction.put("parts", List.of(systemParts));
        requestBody.put("system_instruction", systemInstruction);

        // Contents 설정
        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", userMessage);
        content.put("parts", List.of(parts));
        
        requestBody.put("contents", List.of(content));

        // Generation config 설정
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", geminiConfig.getTemperature());
        generationConfig.put("maxOutputTokens", geminiConfig.getMaxTokens());
        requestBody.put("generationConfig", generationConfig);

        return requestBody;
    }

    /**
     * Gemini 응답에서 텍스트 추출
     */
    private String extractTextFromResponse(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
        } catch (Exception e) {
            log.error("응답 파싱 실패: {}", e.getMessage());
        }
        return "응답을 처리할 수 없습니다.";
    }
}
