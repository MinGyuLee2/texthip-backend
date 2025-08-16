package com.texthip.texthip_server.user.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class OnboardingRequestDto {
    // 기본 정보
    private String ageRange;
    private String gender;

    // 직업 정보
    private String job;
    private String jobInfo;

    // 책 취향 (다중 선택)
    private List<String> preferredGenres;
    private List<String> preferredTopics;

    // 독서 습관
    private String readingTime;
    private Integer monthlyReadingGoal;
}