package com.texthip.texthip_server.auth;

import com.texthip.texthip_server.user.User; // User 임포트 추가
import com.texthip.texthip_server.user.UserDetailsImpl; // UserDetailsImpl 임포트 추가
import com.texthip.texthip_server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. 이메일로 User 엔티티를 찾습니다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. 찾은 User 엔티티를 UserDetailsImpl로 감싸서 반환합니다.
        return new UserDetailsImpl(user);
    }
}