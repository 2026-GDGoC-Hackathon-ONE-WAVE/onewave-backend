package com.example.OneWave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // 👈 import 필수

@EnableJpaAuditing
@SpringBootApplication
public class OneWaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneWaveApplication.class, args);
	}

}