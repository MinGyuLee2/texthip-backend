package com.texthip.texthip_server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserRepository;
import com.texthip.texthip_server.user.UserService;
import com.texthip.texthip_server.user.dto.UserSignupRequestDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트 후 DB 롤백을 위해 사용
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("정상적인 회원가입")
    void signup_success() {
        // given (주어진 상황)
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "test@example.com",
                "password123",
                "tester"
        );

        // when (행동)
        userService.signup(requestDto);

        // then (결과 검증)
        User savedUser = userRepository.findByEmail("test@example.com").orElse(null);
        
        assertNotNull(savedUser); // 사용자가 저장되었는지 확인
        assertEquals("tester", savedUser.getNickname()); // 닉네임이 일치하는지 확인
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword())); // 비밀번호가 암호화되었는지 확인
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 예외 발생")
    void signup_fail_with_duplicate_email() {
        // given (주어진 상황)
        // 먼저 사용자를 하나 저장
        User existingUser = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("existingUser")
                .build();
        userRepository.save(existingUser);

        // 중복된 이메일로 가입 시도
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "test@example.com",
                "password456",
                "newUser"
        );

        // when & then (행동 및 결과 검증)
        // signup 메소드 실행 시 IllegalArgumentException이 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(requestDto);
        });
    }
}