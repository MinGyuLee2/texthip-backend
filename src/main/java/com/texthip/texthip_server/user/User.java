package com.texthip.texthip_server.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 시스템의 사용자를 나타내는 핵심 JPA 엔티티 클래스입니다.
 * 'users' 테이블과 매핑되며, 사용자의 기본 정보 및 다른 엔티티와의 연관관계를 정의합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    /**
     * 사용자의 고유 식별자입니다. (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자의 이메일 주소입니다. 로그인 시 ID로 사용되며, 고유한 값이어야 합니다.
     */
    @Column(length = 255, unique = true, nullable = false)
    private String email;

    /**
     * 암호화되어 저장되는 사용자의 비밀번호입니다.
     */
    @Column(length = 255, nullable = false)
    private String password;

    /**
     * 사용자의 닉네임입니다. 고유한 값이어야 합니다.
     */
    @Column(length = 50, unique = true, nullable = false)
    private String nickname;

    /**
     * 사용자의 프로필 이미지 URL입니다.
     */
    @Column(length = 255)
    private String profileImageUrl;

    /**
     * 사용자의 권한을 나타냅니다. 기본값은 'ROLE_USER'입니다.
     */
    @Column(length = 20, nullable = false)
    private String role = "ROLE_USER";

    /**
     * 레코드 생성 시간입니다.
     */
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * 레코드 마지막 수정 시간입니다.
     */
    @UpdateTimestamp
    private Timestamp updatedAt;

    // --- 연관관계 매핑 ---

    /**
     * 사용자의 상세 프로필 정보입니다. User와 일대일(1:1) 관계입니다.
     * User가 생성되거나 삭제될 때 UserProfile도 함께 관리됩니다 (cascade = CascadeType.ALL).
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    /**
     * 사용자가 선호하는 장르 목록입니다. UserPreferredGenre와 일대다(1:N) 관계입니다.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPreferredGenre> preferredGenres = new ArrayList<>();

    /**
     * 사용자가 선호하는 주제 목록입니다. UserPreferredTopic과 일대다(1:N) 관계입니다.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPreferredTopic> preferredTopics = new ArrayList<>();

    /**
     * User 객체 생성을 위한 빌더 패턴입니다.
     * User가 생성될 때, 해당 User와 연결된 빈 UserProfile 객체도 함께 생성하여 NullPointerException을 방지합니다.
     */
    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        // User 생성 시 UserProfile도 함께 생성하여 관계를 설정
        this.userProfile = UserProfile.builder().user(this).build();
    }
}
