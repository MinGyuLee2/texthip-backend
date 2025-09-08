package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.BookDetailResponseDto;
import com.texthip.texthip_server.book.dto.SearchRequestDto;
import com.texthip.texthip_server.common.CustomPageResponseDto;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    // 특정 도서 정보 조회 API (예: /api/books/9788937460731)
    @GetMapping("/{isbn}")
    public ResponseEntity<BookDetailResponseDto> getBookDetails(@PathVariable String isbn) {
        BookDetailResponseDto bookDetails = bookService.getBookDetails(isbn);
        return ResponseEntity.ok(bookDetails);
    }

    // 도서 검색 API (예: /api/books/search?title=어린왕자&page=0&size=10)
    @PostMapping("/search")
    public ResponseEntity<CustomPageResponseDto<BookDetailResponseDto>> searchBooksByKeyword(
            @RequestBody SearchRequestDto request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        CustomPageResponseDto<BookDetailResponseDto> books =
                bookService.searchBooksByKeyword(request.getQuery(), pageable);

        return ResponseEntity.ok(books);
    }

    // 베스트셀러 목록 조회 API (예: /api/books/bestsellers?size=5)
    @GetMapping("/bestsellers")
    public ResponseEntity<List<BookDetailResponseDto>> getBestsellers(
            @RequestParam(defaultValue = "5") int size
    ) {
        List<BookDetailResponseDto> bestsellers = bookService.getBestsellerList(size);
        return ResponseEntity.ok(bestsellers);
    }
}
