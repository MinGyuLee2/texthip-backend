package com.texthip.texthip_server.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 로그인에 성공했을 때 호출되는 핸들러입니다.
 * 로그인한 사용자의 정보를 기반으로 JWT를 생성하고, 이 토큰을 쿼리 파라미터에 담아
 * 프론트엔드 애플리케이션으로 리디렉션시키는 역할을 합니다.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 1. 인증된 사용자 정보를 OAuth2User 객체로 캐스팅합니다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        
        // 2. 이메일을 기반으로 JWT를 생성합니다.
        String token = jwtTokenProvider.generateTokenFromEmail(email);

        // 3. 프론트엔드 리디렉션 URL에 토큰을 쿼리 파라미터로 추가합니다.
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/redirect") // 프론트엔드 리디렉션 URL
                .queryParam("token", token)
                .build().toUriString();
        
        // 4. 최종 URL로 사용자를 리디렉션시킵니다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}