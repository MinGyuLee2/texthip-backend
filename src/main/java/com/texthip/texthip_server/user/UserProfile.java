package com.texthip.texthip_server.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // User의 ID를 UserProfile의 ID로 사용
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 10)
    private String ageRange;

    @Column(length = 10)
    private String gender;

    @Column(length = 20)
    private String job;

    @Column(length = 255)
    private String jobInfo;

    @Column(length = 10)
    private String readingTime;

    private Integer monthlyReadingGoal;

    @Builder
    public UserProfile(User user) {
        this.user = user;
    }

    public void updateProfile(String ageRange, String gender, String job, String jobInfo, String readingTime, Integer monthlyReadingGoal) {
        this.ageRange = ageRange;
        this.gender = gender;
        this.job = job;
        this.jobInfo = jobInfo;
        this.readingTime = readingTime;
        this.monthlyReadingGoal = monthlyReadingGoal;
    }
}