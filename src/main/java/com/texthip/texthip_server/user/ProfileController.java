package com.texthip.texthip_server.user;

import com.texthip.texthip_server.common.SuccessResponseDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/onboarding")
    public ResponseEntity<SuccessResponseDto> saveOnboardingInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OnboardingRequestDto requestDto) {
        
        profileService.saveOnboardingInfo(userDetails.getUsername(), requestDto);
        return ResponseEntity.ok(new SuccessResponseDto(HttpStatus.OK.value(), "온보딩 정보가 성공적으로 저장되었습니다."));
    }
}