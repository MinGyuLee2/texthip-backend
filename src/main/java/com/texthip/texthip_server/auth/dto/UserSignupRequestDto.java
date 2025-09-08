package com.texthip.texthip_server.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 시 클라이언트로부터 이메일, 비밀번호, 닉네임을 전달받기 위한 DTO입니다.
 */
@Getter
@AllArgsConstructor
public class UserSignupRequestDto {
    private String email;
    private String password;
    private String nickname;
}