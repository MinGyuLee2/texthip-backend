package com.texthip.texthip_server.config;

import com.texthip.texthip_server.auth.CustomOAuth2UserService;
import com.texthip.texthip_server.auth.JwtAuthenticationFilter;
import com.texthip.texthip_server.auth.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 관련 설정을 담당하는 클래스입니다.
 */
@Configuration
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈을 등록합니다.
     * BCrypt 알고리즘을 사용합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 인증을 총괄하는 AuthenticationManager 빈을 등록합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * HTTP 요청에 대한 보안 설정을 정의하는 SecurityFilterChain 빈을 등록합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화합니다. (Stateless한 JWT 방식에서는 불필요)
                .csrf(csrf -> csrf.disable())
                // 서버가 세션을 생성하거나 사용하지 않도록 설정합니다. (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 각 HTTP 요청에 대한 접근 권한을 설정합니다.
                .authorizeHttpRequests(auth -> auth
                        // 아래 경로들은 인증 없이 누구나 접근할 수 있도록 허용합니다.
                        .requestMatchers("/api/auth/signup", "/api/auth/login", "/api/books/bestsellers", "/api/books/search").permitAll()
                        // 그 외 모든 요청은 반드시 인증을 거쳐야 합니다.
                        .anyRequest().authenticated()
                )
                // OAuth2 소셜 로그인 관련 설정을 정의합니다.
                .oauth2Login(oauth2 -> oauth2
                        // 소셜 로그인 성공 후 사용자 정보를 가져올 때 사용할 서비스를 지정합니다.
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        // OAuth2 로그인 성공 시 호출될 핸들러를 등록합니다.
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                );

        // 직접 구현한 JWT 인증 필터를 Spring Security의 기본 로그인 필터(UsernamePasswordAuthenticationFilter) 앞에 추가합니다.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
