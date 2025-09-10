package com.texthip.texthip_server.auth.dto;

import lombok.Getter;

/**
 * 일반 로그인 시 클라이언트로부터 이메일과 비밀번호를 전달받기 위한 DTO입니다.
 */
@Getter
public class UserLoginRequestDto {
    private String email;
    private String password;
}