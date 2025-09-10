package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.BookDetailResponseDto;
import com.texthip.texthip_server.book.dto.SearchRequestDto;
import com.texthip.texthip_server.common.ApiResponse;
import com.texthip.texthip_server.common.CustomPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * '/api/books' 경로의 도서 관련 API 요청을 처리하는 컨트롤러입니다.
 * (도서 검색, 상세 정보 조회, 베스트셀러 조회)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    /**
     * ISBN으로 특정 도서의 상세 정보를 조회하는 API
     * @param isbn 조회할 도서의 13자리 ISBN
     * @return 도서 상세 정보를 담은 ApiResponse
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<ApiResponse<BookDetailResponseDto>> getBookDetails(@PathVariable String isbn) {
        BookDetailResponseDto bookDetails = bookService.getBookDetails(isbn);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "도서 상세 정보 조회 성공", bookDetails));
    }

    /**
     * 키워드로 도서를 검색하는 API
     * @param request 검색어(query)와 페이징 정보(page, size)를 담은 DTO
     * @return 페이징된 도서 검색 결과를 담은 ApiResponse
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<CustomPageResponseDto<BookDetailResponseDto>>> searchBooksByKeyword(
            @RequestBody SearchRequestDto request) {

        // 요청 DTO에서 받은 페이지와 사이즈 정보로 Pageable 객체 생성
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        CustomPageResponseDto<BookDetailResponseDto> books =
                bookService.searchBooksByKeyword(request.getQuery(), pageable);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "도서 검색 성공", books));
    }

    /**
     * 베스트셀러 목록을 조회하는 API
     * @param size 조회할 베스트셀러의 개수 (기본값: 5)
     * @return 베스트셀러 도서 목록을 담은 ApiResponse
     */
    @GetMapping("/bestsellers")
    public ResponseEntity<ApiResponse<List<BookDetailResponseDto>>> getBestsellers(
            @RequestParam(defaultValue = "5") int size) {
        List<BookDetailResponseDto> bestsellers = bookService.getBestsellerList(size);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "베스트셀러 조회 성공", bestsellers));
    }
}

