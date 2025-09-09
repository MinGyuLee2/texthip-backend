package com.texthip.texthip_server.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자의 상세 프로필 정보를 나타내는 JPA 엔티티 클래스입니다.
 * 'user_profiles' 테이블과 매핑됩니다. User 엔티티와 일대일 관계를 가집니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_profiles")
public class UserProfile {

    /**
     * 프로필의 고유 식별자입니다. User 엔티티의 ID를 공유합니다.
     */
    @Id
    private Long userId;

    /**
     * 이 프로필의 소유자인 User 엔티티입니다.
     * @MapsId: 이 관계를 통해 User의 ID가 이 엔티티의 기본 키(userId)에 매핑되도록 합니다.
     * 즉, User와 UserProfile은 항상 같은 ID 값을 가집니다.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // 연령대
    @Column(length = 10)
    private String ageRange;

    // 성별
    @Column(length = 10)
    private String gender;

    // 직업
    @Column(length = 20)
    private String job;

    // 직업 상세 정보
    @Column(length = 255)
    private String jobInfo;

    // 주로 책을 읽는 시간대
    @Column(length = 10)
    private String readingTime;

    // 월간 독서 목표량
    private Integer monthlyReadingGoal;

    @Builder
    public UserProfile(User user) {
        this.user = user;
    }

    /**
     * 온보딩 정보로 프로필을 업데이트하는 메소드입니다.
     */
    public void updateProfile(String ageRange, String gender, String job, String jobInfo, String readingTime, Integer monthlyReadingGoal) {
        this.ageRange = ageRange;
        this.gender = gender;
        this.job = job;
        this.jobInfo = jobInfo;
        this.readingTime = readingTime;
        this.monthlyReadingGoal = monthlyReadingGoal;
    }
}
