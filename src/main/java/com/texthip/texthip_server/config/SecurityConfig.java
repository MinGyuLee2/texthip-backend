package com.texthip.texthip_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.texthip.texthip_server.user.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor; 

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

     private final CustomOAuth2UserService customOAuth2UserService;

    // 이 메소드가 PasswordEncoder를 Bean으로 등록해줍니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 이 메소드는 API 경로별 접근 권한을 설정합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 비활성화 (API 서버에서는 일반적으로 비활성화)
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(auth -> auth
                // '/api/users/signup', '/health' 경로는 인증 없이 누구나 접근 허용
                .requestMatchers("/api/users/signup", "/health").permitAll()
                // 나머지 모든 요청은 인증 필요 (나중에 API를 추가하면서 수정)
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
        );

        return http.build();
    }
}
