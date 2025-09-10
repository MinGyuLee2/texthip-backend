package com.texthip.texthip_server.auth;

import com.texthip.texthip_server.auth.dto.TokenResponseDto;
import com.texthip.texthip_server.auth.dto.UserLoginRequestDto;
import com.texthip.texthip_server.auth.dto.UserSignupRequestDto;
import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * (회원가입, 로그인)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자를 등록(회원가입)하는 메소드입니다.
     * @param requestDto 회원가입 요청 DTO
     */
    @Transactional
    public void signup(UserSignupRequestDto requestDto) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        // 닉네임 중복 확인
        if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // User 객체 생성
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .nickname(requestDto.getNickname())
                .build();

        // DB에 사용자 정보 저장
        userRepository.save(user);
    }

    /**
     * 사용자를 인증하고 JWT를 발급하는 로그인 메소드입니다.
     * @param loginRequest 로그인 요청 DTO
     * @return Access Token을 담은 DTO
     */
    @Transactional
    public TokenResponseDto login(UserLoginRequestDto loginRequest) {
        // 1. Spring Security의 AuthenticationManager를 사용하여 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // 2. 인증 정보를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 인증된 사용자 정보를 기반으로 JWT 생성
        String jwt = jwtTokenProvider.generateToken(authentication);
        return new TokenResponseDto(jwt);
    }
}