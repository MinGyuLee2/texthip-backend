package com.texthip.texthip_server;

import com.texthip.texthip_server.user.ProfileService;
import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserProfile;
import com.texthip.texthip_server.user.UserProfileRepository;
import com.texthip.texthip_server.user.UserRepository;
import com.texthip.texthip_server.user.dto.OnboardingRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@TestPropertySource(properties = {"jwt.secret.key=V2VMa2VUZXh0SGlwQmVzdFNlcnZpY2VGb3JNeUxpZmUhIQ=="})
class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 테스트를 실행할 사용자 미리 생성
        testUser = User.builder()
                .email("test33@example.com")
                .password("password123")
                .nickname("tester")
                .build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("온보딩 정보 저장 성공")
    void saveOnboardingInfo_success() {
        // given (주어진 상황)
        OnboardingRequestDto requestDto = new OnboardingRequestDto(
                "20s",
                "MALE",
                "STUDENT",
                "컴퓨터공학",
                List.of("소설", "과학"),
                List.of("기술", "환경"),
                "NIGHT",
                10
        );

        // when (행동)
        profileService.saveOnboardingInfo(testUser.getEmail(), requestDto);

        // then (결과 검증)
        UserProfile savedProfile = userProfileRepository.findById(testUser.getId()).orElse(null);

        assertNotNull(savedProfile);
        assertEquals("20s", savedProfile.getAgeRange());
        assertEquals("STUDENT", savedProfile.getJob());

        User updatedUser = userRepository.findByEmail(testUser.getEmail()).get();
        assertEquals(2, updatedUser.getPreferredGenres().size());
        assertEquals(2, updatedUser.getPreferredTopics().size());
        assertEquals("소설", updatedUser.getPreferredGenres().get(0).getGenreName());
    }
}