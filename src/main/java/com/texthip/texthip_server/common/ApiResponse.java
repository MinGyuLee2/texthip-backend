package com.texthip.texthip_server.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API의 모든 성공 응답을 감싸는 표준 제네릭 래퍼 클래스입니다.
 * 이 클래스를 사용함으로써 클라이언트는 항상 일관된 형식의 응답을 받을 수 있습니다.
 *
 * @param <T> 응답의 'data' 필드에 포함될 데이터의 타입
 */
@Getter
public class ApiResponse<T> {

    // HTTP 상태 코드 (예: 200, 201)
    private final int statusCode;
    // 응답 메시지 (예: "로그인 성공")
    private final String message;

    // 실제 응답 데이터. null일 경우 JSON 직렬화 시 포함되지 않음
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    /**
     * 데이터가 없는 성공 응답을 위한 private 생성자.
     * 정적 팩토리 메소드 'success'를 통해 호출됩니다.
     */
    private ApiResponse(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.message = message;
        this.data = null;
    }

    /**
     * 데이터가 있는 성공 응답을 위한 private 생성자.
     * 정적 팩토리 메소드 'success'를 통해 호출됩니다.
     */
    private ApiResponse(HttpStatus status, String message, T data) {
        this.statusCode = status.value();
        this.message = message;
        this.data = data;
    }

    /**
     * 데이터가 없는 성공 응답 객체를 생성하는 정적 팩토리 메소드.
     * @param status HTTP 상태 코드
     * @param message 응답 메시지
     * @return ApiResponse<Void> 객체
     */
    public static ApiResponse<Void> success(HttpStatus status, String message) {
        return new ApiResponse<>(status, message);
    }

    /**
     * 데이터가 있는 성공 응답 객체를 생성하는 정적 팩토리 메소드.
     * @param status HTTP 상태 코드
     * @param message 응답 메시지
     * @param data 클라이언트에게 전달할 실제 데이터
     * @param <T> 데이터의 타입
     * @return ApiResponse<T> 객체
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}

