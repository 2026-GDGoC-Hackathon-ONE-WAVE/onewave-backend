package com.example.OneWave.service;

import com.example.OneWave.domain.Application;
import com.example.OneWave.dto.DashboardResponse;
import com.example.OneWave.repository.ApplicationRepository;
import com.example.OneWave.repository.ReflectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ApplicationRepository applicationRepository;
    private final ReflectionRepository reflectionRepository;

    public DashboardResponse getDashboardData(Long userId) {
        // 1. 전체 지원 내역 및 회고 데이터 가져오기
        List<Application> allApps = applicationRepository.findAllByUser_UserId(userId);

        // 2. 요약 데이터 계산
        long totalReflections = allApps.stream()
                .filter(app -> app.getReflection() != null)
                .count();

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long thisMonthReflections = allApps.stream()
                .filter(app -> app.getReflection() != null && app.getReflection().getCreatedAt().isAfter(startOfMonth))
                .count();

        // 3. 단계별 탈락률 계산 (Grouping By failedStage)
        Map<String, Long> failureMap = allApps.stream()
                .filter(app -> app.getFailedStage() != null)
                .collect(Collectors.groupingBy(Application::getFailedStage, Collectors.counting()));

        List<DashboardResponse.StageFailureRateDto> failureRates = failureMap.entrySet().stream()
                .map(entry -> {
                    int rate = (int) ((double) entry.getValue() / allApps.size() * 100);
                    return DashboardResponse.StageFailureRateDto.builder()
                            .stage(entry.getKey())
                            .failureCount(entry.getValue())
                            .totalCount(allApps.size())
                            .failureRate(rate)
                            .description(getStageDescription(entry.getKey())) // 헬퍼 메서드로 설명 매핑
                            .build();
                })
                .sorted(Comparator.comparing(DashboardResponse.StageFailureRateDto::getFailureCount).reversed())
                .collect(Collectors.toList());

        // 4. 인기 키워드 TOP 3 (예시 데이터)
        // 실제로는 ReflectionKeyword 테이블에서 count 쿼리를 날려야 합니다.
        List<DashboardResponse.TopKeywordDto> topKeywords = List.of(
                DashboardResponse.TopKeywordDto.builder().keyword("성장").count(12).build(),
                DashboardResponse.TopKeywordDto.builder().keyword("몰입").count(8).build(),
                DashboardResponse.TopKeywordDto.builder().keyword("성취").count(6).build()
        );

        // 5. 월별 회고 횟수
        Map<String, Long> monthlyMap = allApps.stream()
                .filter(app -> app.getReflection() != null)
                .collect(Collectors.groupingBy(
                        app -> app.getReflection().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.counting()
                ));

        List<DashboardResponse.MonthlyCountDto> monthlyCounts = monthlyMap.entrySet().stream()
                .map(entry -> DashboardResponse.MonthlyCountDto.builder()
                        .month(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .sorted(Comparator.comparing(DashboardResponse.MonthlyCountDto::getMonth))
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .userId(userId)
                .summary(DashboardResponse.SummaryDto.builder()
                        .totalReflections(totalReflections)
                        .thisMonthReflections(thisMonthReflections)
                        .build())
                .stageFailureRates(failureRates)
                .topKeywords(topKeywords)
                .monthlyReflectionCount(monthlyCounts)
                .build();
    }

    // 단계별 고정 설명 매핑 (데이터가 없을 경우의 기본값)
    private String getStageDescription(String stage) {
        return switch (stage) {
            case "최종 면접" -> "답변 질문에 대한 준비가 부족했던 경험이 있습니다.";
            case "코딩 테스트" -> "시간 관리를 위해 더 효율적인 알고리즘 접근이 필요합니다.";
            case "서류 전형" -> "직무 적합도를 강조하는 비중을 높여보세요.";
            default -> "자신감을 가지고 다음 단계를 준비하세요.";
        };
    }
}