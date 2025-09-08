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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody UserSignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "회원가입이 성공적으로 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@RequestBody UserLoginRequestDto loginRequest) {
        TokenResponseDto token = userService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "로그인에 성공했습니다.", token));
    }
}
