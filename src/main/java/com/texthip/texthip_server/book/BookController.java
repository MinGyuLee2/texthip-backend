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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{isbn}")
    public ResponseEntity<ApiResponse<BookDetailResponseDto>> getBookDetails(@PathVariable String isbn) {
        BookDetailResponseDto bookDetails = bookService.getBookDetails(isbn);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "도서 상세 정보 조회 성공", bookDetails));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<CustomPageResponseDto<BookDetailResponseDto>>> searchBooksByKeyword(
            @RequestBody SearchRequestDto request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        CustomPageResponseDto<BookDetailResponseDto> books =
                bookService.searchBooksByKeyword(request.getQuery(), pageable);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "도서 검색 성공", books));
    }

    @GetMapping("/bestsellers")
    public ResponseEntity<ApiResponse<List<BookDetailResponseDto>>> getBestsellers(
            @RequestParam(defaultValue = "5") int size) {
        List<BookDetailResponseDto> bestsellers = bookService.getBestsellerList(size);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "베스트셀러 조회 성공", bestsellers));
    }
}
