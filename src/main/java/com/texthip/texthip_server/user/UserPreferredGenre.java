package com.texthip.texthip_server.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 선호하는 장르를 나타내는 JPA 엔티티 클래스입니다.
 * 'user_preferred_genres' 테이블과 매핑됩니다. User와 다대일 관계를 가집니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_preferred_genres")
public class UserPreferredGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 선호 장르를 소유한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 선호하는 장르의 이름
    @Column(length = 50, nullable = false)
    private String genreName;

    public UserPreferredGenre(User user, String genreName) {
        this.user = user;
        this.genreName = genreName;
    }
}
