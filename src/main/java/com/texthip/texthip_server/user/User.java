package com.texthip.texthip_server.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User implements UserDetails { // UserDetails 인터페이스 구현 추가

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, unique = true, nullable = false)
    private String nickname;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 20, nullable = false)
    private String role = "ROLE_USER"; // 기본값 설정

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    // --- UserDetails 인터페이스의 필수 메소드 구현 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 'role' 필드를 기반으로 권한을 반환
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        // 사용자 식별자로는 email을 사용
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 (true: 만료되지 않음)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 (true: 잠기지 않음)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명 만료 여부 (true: 만료되지 않음)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부 (true: 활성화됨)
        return true;
    }
}