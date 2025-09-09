package com.texthip.texthip_server.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security가 사용자의 인증/인가 정보를 인식할 수 있도록,
 * User 엔티티를 Spring Security의 UserDetails 인터페이스로 감싸주는(Adaptor) 클래스입니다.
 */
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * Spring Security에서 사용자를 식별하는 username을 반환합니다.
     * 이 시스템에서는 이메일을 username으로 사용합니다.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * 사용자의 암호화된 비밀번호를 반환합니다.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자에게 부여된 권한 목록을 반환합니다.
     * User 엔티티의 'role' 필드를 기반으로 권한(예: "ROLE_USER")을 생성합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    // --- 계정 상태 관련 메소드들 ---
    // 특별한 비활성화/만료 정책이 없으므로 모두 true를 반환합니다.

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 (true: 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true: 활성화됨)
    }
}
