package com.example.OneWave.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model}")
    private String model;

    @Value("${gemini.api.temperature}")
    private Double temperature;

    @Value("${gemini.api.max-tokens}")
    private Integer maxTokens;
}
