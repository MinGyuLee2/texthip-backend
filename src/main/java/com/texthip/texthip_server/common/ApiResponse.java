package com.texthip.texthip_server.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int statusCode;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) // data 필드가 null일 경우, JSON에서 제외
    private final T data;

    // 데이터가 없는 성공 응답을 위한 생성자
    private ApiResponse(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.message = message;
        this.data = null;
    }

    // 데이터가 있는 성공 응답을 위한 생성자
    private ApiResponse(HttpStatus status, String message, T data) {
        this.statusCode = status.value();
        this.message = message;
        this.data = data;
    }

    // 성공 응답 (데이터 없음)
    public static ApiResponse<Void> success(HttpStatus status, String message) {
        return new ApiResponse<>(status, message);
    }

    // 성공 응답 (데이터 있음)
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}
