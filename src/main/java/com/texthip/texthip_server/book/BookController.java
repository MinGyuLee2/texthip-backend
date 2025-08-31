package com.texthip.texthip_server.book;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    @GetMapping("/search")
    public ResponseEntity<Page<BookDetailResponseDto>> searchBooks(@RequestParam String title, Pageable pageable) {
        Page<BookDetailResponseDto> books = bookService.searchBooksByTitle(title, pageable);
        return ResponseEntity.ok(books);
    }

    // 베스트셀러 목록 조회 API (예: /api/books/bestsellers?size=5)
    @GetMapping("/bestsellers")
    public ResponseEntity<List<BookDetailResponseDto>> getBestsellers(
            @RequestParam(defaultValue = "5") int size //클라이언트 요청이 없으면 기본 5개 
    ) {
        List<BookDetailResponseDto> bestsellerList = bookService.getBestsellerList(size);
        return ResponseEntity.ok(bestsellerList);
    }
}