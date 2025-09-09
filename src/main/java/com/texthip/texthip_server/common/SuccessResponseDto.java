package com.texthip.texthip_server.common;

import lombok.Getter;

/**
 * 데이터 없이 단순 성공 메시지만을 전달하기 위한 DTO입니다.
 * @deprecated 현재는 `ApiResponse<Void>`로 대체되어 사용되지 않습니다.
 */
@Getter
public class SuccessResponseDto {
    private int statusCode;
    private String message;

    public SuccessResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
