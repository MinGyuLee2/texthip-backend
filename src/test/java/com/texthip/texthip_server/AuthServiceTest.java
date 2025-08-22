package com.texthip.texthip_server;

import com.texthip.texthip_server.auth.AuthService;
import com.texthip.texthip_server.auth.dto.UserSignupRequestDto;
import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest { 

    @Autowired
    private AuthService authService; 

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("정상적인 회원가입")
    void signup_success() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "test@example.com",
                "password123",
                "tester"
        );

        // when
        authService.signup(requestDto); 

        // then
        User savedUser = userRepository.findByEmail("test@example.com").orElse(null);
        
        assertNotNull(savedUser);
        assertEquals("tester", savedUser.getNickname());
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 예외 발생")
    void signup_fail_with_duplicate_email() {
        // given
        User existingUser = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("existingUser")
                .build();
        userRepository.save(existingUser);

        UserSignupRequestDto requestDto = new UserSignupRequestDto(
                "test@example.com",
                "password456",
                "newUser"
        );

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            authService.signup(requestDto); 
        });
    }
}