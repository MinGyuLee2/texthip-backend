package com.texthip.texthip_server.exception;

import lombok.Getter;

/**
 * API에서 오류 발생 시, 클라이언트에게 일관된 형식의 에러 응답을 보내기 위한 DTO 클래스입니다.
 */
@Getter
public class ErrorResponseDto {
    // HTTP 상태 코드 (예: 400, 404, 500)
    private int statusCode;
    // 오류 메시지
    private String message;

    public ErrorResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
