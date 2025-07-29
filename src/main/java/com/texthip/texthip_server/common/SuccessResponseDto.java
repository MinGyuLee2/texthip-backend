package com.texthip.texthip_server.common;

import lombok.Getter;

@Getter
public class SuccessResponseDto {
    private int statusCode;
    private String message;

    public SuccessResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}