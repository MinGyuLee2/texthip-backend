package com.texthip.texthip_server.user;

import com.texthip.texthip_server.common.ApiResponse;
import com.texthip.texthip_server.user.dto.OnboardingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * '/api/profiles' 경로의 사용자 프로필 관련 API 요청을 처리하는 컨트롤러입니다.
 * (온보딩 정보 저장 등)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 회원가입 후 사용자의 초기 프로필(온보딩) 정보를 저장하는 API
     * @param userDetails 현재 인증된 사용자의 정보 (JWT 토큰에서 추출)
     * @param requestDto 온보딩 정보(취향, 독서 습관 등)를 담은 DTO
     * @return 성공 메시지를 담은 ApiResponse
     */
    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<Void>> saveOnboardingInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OnboardingRequestDto requestDto) {

        // @AuthenticationPrincipal을 통해 JWT 토큰에 담긴 사용자 정보(UserDetails)를 직접 받아옵니다.
        // userDetails.getUsername()은 UserDetailsImpl에 정의된 대로 사용자의 이메일을 반환합니다.
        profileService.saveOnboardingInfo(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "온보딩 정보가 성공적으로 저장되었습니다."));
    }
}

