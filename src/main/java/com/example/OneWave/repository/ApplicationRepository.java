package com.example.OneWave.repository;

import com.example.OneWave.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUser_UserId(Long userId);

    List<Application> findAllByUser_UserIdOrderByCreatedAtDesc(Long userId);
}