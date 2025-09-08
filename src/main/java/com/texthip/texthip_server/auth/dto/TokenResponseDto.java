package com.texthip.texthip_server.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 성공 시 클라이언트에게 Access Token을 전달하기 위한 DTO입니다.
 */
@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private String accessToken;
}