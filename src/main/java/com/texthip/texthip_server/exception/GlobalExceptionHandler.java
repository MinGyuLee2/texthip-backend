package com.texthip.texthip_server.exception;

import com.texthip.texthip_server.book.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

/**
 * 애플리케이션 전역에서 발생하는 예외를 중앙에서 처리하는 클래스입니다.
 * @RestControllerAdvice 어노테이션을 통해 모든 @RestController에서 발생하는 예외를 가로챕니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 인자(Argument)로 요청이 들어왔을 때 발생하는 IllegalArgumentException을 처리합니다.
     * @return 400 Bad Request 상태 코드와 에러 메시지를 담은 ErrorResponseDto
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * 요청한 리소스를 찾을 수 없을 때(404 Not Found) 발생하는 예외들을 처리합니다.
     * (예: DB에 없는 데이터를 조회하려 할 때)
     * @return 404 Not Found 상태 코드와 에러 메시지를 담은 ErrorResponseDto
     */
    @ExceptionHandler({BookNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(RuntimeException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    /**
     * 특정 리소스에 대한 접근 권한이 없을 때 발생하는 AccessDeniedException을 처리합니다.
     * @return 403 Forbidden 상태 코드와 에러 메시지를 담은 ErrorResponseDto
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(HttpStatus.FORBIDDEN.value(), "요청에 대한 접근 권한이 없습니다.");
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    /**
     * 위에서 처리되지 않은 모든 예외를 최종적으로 처리합니다.
     * @return 500 Internal Server Error 상태 코드와 에러 메시지를 담은 ErrorResponseDto
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다: " + ex.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

