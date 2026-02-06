package com.example.OneWave.repository;

import com.example.OneWave.domain.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    
    @Query("SELECT cs FROM ChatSession cs " +
           "JOIN FETCH cs.application a " +
           "JOIN FETCH a.user u " +
           "WHERE cs.sessionId = :sessionId")
    Optional<ChatSession> findByIdWithApplicationAndUser(@Param("sessionId") Long sessionId);
}
