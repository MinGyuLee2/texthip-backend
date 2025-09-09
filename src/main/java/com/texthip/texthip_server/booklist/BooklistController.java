package com.texthip.texthip_server.booklist;

import com.texthip.texthip_server.booklist.dto.AddBookRequestDto;
import com.texthip.texthip_server.booklist.dto.BooklistCreateRequestDto;
import com.texthip.texthip_server.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

/**
 * '/api/booklists' 경로의 북리스트 관련 API 요청을 처리하는 컨트롤러입니다.
 * (북리스트 생성, 북리스트에 책 추가 등)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booklists")
public class BooklistController {

    private final BooklistService booklistService;

    /**
     * 새로운 북리스트를 생성하는 API
     * @param requestDto 북리스트 생성에 필요한 정보 (제목, 설명)
     * @param userDetails 현재 인증된 사용자의 정보 (JWT 토큰에서 추출)
     * @return 생성 성공 메시지를 담은 ApiResponse
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBooklist(
            @RequestBody BooklistCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 현재 로그인한 사용자의 이메일(username)을 서비스 계층으로 전달
        booklistService.createBooklist(requestDto, userDetails.getUsername());
        // HTTP 201 Created 상태 코드와 함께 성공 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "북리스트가 생성되었습니다."));
    }

    /**
     * 특정 북리스트에 책을 추가하는 API
     * @param booklistId 책을 추가할 북리스트의 ID
     * @param requestDto 추가할 책의 ISBN 정보
     * @param userDetails 현재 인증된 사용자의 정보
     * @return 추가 성공 메시지를 담은 ApiResponse
     * @throws AccessDeniedException 북리스트 소유자가 아닐 경우
     */
    @PostMapping("/{booklistId}/books")
    public ResponseEntity<ApiResponse<Void>> addBookToBooklist(
            @PathVariable Long booklistId,
            @RequestBody AddBookRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {

        booklistService.addBookToBooklist(booklistId, requestDto.getIsbn(), userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "북리스트에 책이 추가되었습니다."));
    }
}

