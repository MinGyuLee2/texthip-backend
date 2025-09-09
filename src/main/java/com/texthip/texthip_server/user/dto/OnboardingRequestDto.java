package com.texthip.texthip_server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * 온보딩 정보 저장 API(POST /api/profiles/onboarding) 요청 시,
 * 클라이언트로부터 사용자의 초기 프로필 정보를 전달받기 위한 DTO입니다.
 */
@Getter
@AllArgsConstructor
public class OnboardingRequestDto {
    // 기본 정보
    private String ageRange;
    private String gender;

    // 직업 정보
    private String job;
    private String jobInfo;

    // 책 취향 (다중 선택 가능)
    private List<String> preferredGenres;
    private List<String> preferredTopics;

    // 독서 습관
    private String readingTime;
    private Integer monthlyReadingGoal;
}

