package com.texthip.texthip_server.auth.dto;

import lombok.Getter;

@Getter
public class UserLoginRequestDto {
    private String email;
    private String password;
}