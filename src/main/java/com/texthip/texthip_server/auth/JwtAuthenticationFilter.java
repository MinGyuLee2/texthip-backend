package com.texthip.texthip_server.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 API 요청에 대해 한 번씩 실행되는 Spring Security 필터입니다.
 * 요청 헤더의 'Authorization'에 포함된 JWT를 검증하고, 유효한 경우 해당 사용자의 인증 정보를
 * SecurityContextHolder에 설정하여 인증된 사용자로 만듭니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService; // CustomUserDetailsService가 주입됨

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. 요청 헤더에서 JWT를 추출합니다.
            String jwt = getJwtFromRequest(request);

            // 2. JWT가 유효한지 검증합니다.
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // 3. 토큰에서 사용자 이메일을 추출합니다.
                String userEmail = tokenProvider.getUsernameFromToken(jwt);
                // 4. 이메일을 사용하여 DB에서 사용자 정보를 조회합니다.
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                // 5. 인증 토큰(UsernamePasswordAuthenticationToken)을 생성합니다.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. SecurityContextHolder에 인증 정보를 설정합니다.
                // 이제 이 요청은 인증된 사용자의 요청으로 처리됩니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Security Context에 사용자 인증 정보를 설정할 수 없습니다.", ex);
        }

        // 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }

    /**
     * HttpServletRequest의 'Authorization' 헤더에서 'Bearer ' 접두사를 제거하고 순수한 토큰 문자열만 추출합니다.
     * @param request HttpServletRequest 객체
     * @return 'Bearer '가 제거된 JWT 문자열 또는 null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}