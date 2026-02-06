package com.example.OneWave.repository;

import com.example.OneWave.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}