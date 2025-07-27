package com.texthip.texthip_server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor 
public class UserSignupRequestDto {
    private String email;
    private String password;
    private String nickname;
}

