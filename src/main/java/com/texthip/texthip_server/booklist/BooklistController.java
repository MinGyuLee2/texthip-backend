package com.texthip.texthip_server.booklist;

import com.texthip.texthip_server.booklist.dto.AddBookRequestDto;
import com.texthip.texthip_server.booklist.dto.BooklistCreateRequestDto;
import com.texthip.texthip_server.common.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booklists")
public class BooklistController {

    private final BooklistService booklistService;

    // 새 북리스트 생성
    @PostMapping
    public ResponseEntity<SuccessResponseDto> createBooklist(
            @RequestBody BooklistCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        booklistService.createBooklist(requestDto, userDetails.getUsername());
        return ResponseEntity.ok(new SuccessResponseDto(HttpStatus.OK.value(), "북리스트가 생성되었습니다."));
    }

    // 북리스트에 책 추가
    @PostMapping("/{booklistId}/books")
    public ResponseEntity<SuccessResponseDto> addBookToBooklist(
            @PathVariable Long booklistId,
            @RequestBody AddBookRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        
        booklistService.addBookToBooklist(booklistId, requestDto.getIsbn(), userDetails.getUsername());
        return ResponseEntity.ok(new SuccessResponseDto(HttpStatus.OK.value(), "북리스트에 책이 추가되었습니다."));
    }
}
