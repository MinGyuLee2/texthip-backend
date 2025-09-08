package com.texthip.texthip_server.auth;

import com.texthip.texthip_server.auth.dto.TokenResponseDto;
import com.texthip.texthip_server.auth.dto.UserLoginRequestDto;
import com.texthip.texthip_server.auth.dto.UserSignupRequestDto;
import com.texthip.texthip_server.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * '/api/auth' 경로의 인증 관련 API 요청을 처리하는 컨트롤러입니다.
 * (회원가입, 로그인)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 회원가입 API
     * @param requestDto 회원가입에 필요한 정보 (이메일, 비밀번호, 닉네임)
     * @return 성공 메시지를 담은 ApiResponse
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody UserSignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "회원가입이 성공적으로 완료되었습니다."));
    }

    /**
     * 사용자 로그인 API
     * @param loginRequest 로그인에 필요한 정보 (이메일, 비밀번호)
     * @return Access Token을 담은 ApiResponse
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@RequestBody UserLoginRequestDto loginRequest) {
        TokenResponseDto token = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "로그인에 성공했습니다.", token));
    }
}