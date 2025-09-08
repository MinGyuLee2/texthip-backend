package com.texthip.texthip_server.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * JWT(JSON Web Token)의 생성, 검증, 정보 추출 등 토큰 관련 모든 기능을 담당하는 유틸리티 클래스입니다.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    // application.yml에 정의된 JWT secret key
    @Value("${jwt.secret}")
    private String jwtSecret;

    // application.yml에 정의된 JWT 만료 시간 (밀리초 단위)
    @Value("${jwt.expiration-ms}")
    private int jwtExpirationInMs;

    private Key key;

    /**
     * 객체 생성 후, 주입받은 secret 값을 기반으로 JWT 서명에 사용할 Key 객체를 초기화합니다.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 인증된 사용자 정보(Authentication 객체)를 기반으로 JWT를 생성합니다.
     * @param authentication Spring Security의 인증 정보
     * @return 생성된 JWT 문자열
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // JWT Builder를 사용하여 토큰 생성
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // 토큰의 주체(subject)로 사용자의 이메일을 사용합니다.
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(expiryDate) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 키 설정
                .compact();
    }
    
    /**
     * (OAuth2 로그인 시) 이메일 주소를 기반으로 JWT를 생성합니다.
     * @param email 사용자의 이메일 주소
     * @return 생성된 JWT 문자열
     */
    public String generateTokenFromEmail(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT에서 사용자의 이메일(username)을 추출합니다.
     * @param token JWT 문자열
     * @return 사용자 이메일
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 주어진 JWT가 유효한지 검증합니다.
     * (서명 확인, 만료 여부 확인 등)
     * @param authToken 검증할 JWT 문자열
     * @return 유효하면 true, 아니면 false
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            // 토큰이 유효하지 않을 경우(서명 오류, 형식 오류, 만료 등) 로그를 남기고 false를 반환
            log.error("JWT validation error: {}", ex.getMessage());
        }
        return false;
    }
}