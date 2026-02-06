package com.example.OneWave.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI muluckOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OneWave API")
                        .version("v1.0.0"));

    }
}
