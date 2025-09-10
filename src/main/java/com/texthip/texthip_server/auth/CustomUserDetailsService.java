package com.texthip.texthip_server.auth;

import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserDetailsImpl;
import com.texthip.texthip_server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security의 UserDetailsService 인터페이스를 구현한 클래스입니다.
 * JWT 필터로부터 사용자 식별자(여기서는 이메일)를 받아, DB에서 사용자 정보를 조회하고
 * Spring Security가 이해할 수 있는 UserDetails 객체로 변환하는 역할을 합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 이메일을 기반으로 DB에서 사용자 정보를 조회합니다.
     * @param email (loadUserByUsername 메소드의 username 파라미터)
     * @return UserDetails 객체 (UserDetailsImpl)
     * @throws UsernameNotFoundException 해당 이메일의 사용자를 찾을 수 없을 때 발생
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. 이메일로 User 엔티티를 찾습니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 사용자를 찾을 수 없습니다: " + email));

        // 2. 찾은 User 엔티티를 Spring Security가 사용하는 UserDetails 객체로 변환하여 반환합니다.
        return new UserDetailsImpl(user);
    }
}