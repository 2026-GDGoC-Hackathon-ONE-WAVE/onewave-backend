package com.example.OneWave.repository;

import com.example.OneWave.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByUser_UserId(Long userId);
}