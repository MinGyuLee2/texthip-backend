package com.texthip.texthip_server.user;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texthip.texthip_server.common.SuccessResponseDto;
import com.texthip.texthip_server.user.dto.UserSignupRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDto> signup(@RequestBody UserSignupRequestDto requestDto) {
        userService.signup(requestDto);
        SuccessResponseDto response = new SuccessResponseDto(
                HttpStatus.OK.value(), // 200 상태 코드
                "회원가입이 성공적으로 완료되었습니다."
        );
        return ResponseEntity.ok(response);
    }
}