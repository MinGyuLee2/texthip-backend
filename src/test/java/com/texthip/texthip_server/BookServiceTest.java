package com.texthip.texthip_server;

import com.texthip.texthip_server.book.Book;
import com.texthip.texthip_server.book.BookDetailResponseDto;
import com.texthip.texthip_server.book.BookNotFoundException;
import com.texthip.texthip_server.book.BookRepository;
import com.texthip.texthip_server.book.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        // 빌더 패턴으로 객체 생성
        testBook = Book.builder()
                .isbn("9788937460731")
                .title("어린 왕자")
                .author("앙투안 드 생텍쥐페리")
                .publisher("민음사")
                .pageCount(100)
                .build();
                
        bookRepository.save(testBook);
    }

    @Test
    @DisplayName("ISBN으로 도서 상세 정보 조회 성공")
    void getBookDetails_success() {
        // given (주어진 상황)
        String isbn = "9788937460731";

        // when (행동)
        BookDetailResponseDto responseDto = bookService.getBookDetails(isbn);

        // then (결과 검증)
        assertEquals(isbn, responseDto.getIsbn());
        assertEquals("어린 왕자", responseDto.getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 ISBN으로 조회 시 예외 발생")
    void getBookDetails_fail_with_non_existent_isbn() {
        // given (주어진 상황)
        String nonExistentIsbn = "0000000000000";

        // when & then (행동 및 결과 검증)
        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookDetails(nonExistentIsbn);
        });
    }

    @Test
    @DisplayName("제목으로 도서 검색 성공")
    void searchBooksByTitle_success() {
        // given (주어진 상황)
        String query = "왕자";
        Pageable pageable = PageRequest.of(0, 10);

        // when (행동)
        Page<BookDetailResponseDto> results = bookService.searchBooksByTitle(query, pageable);

        // then (결과 검증)
        assertEquals(1, results.getTotalElements());
        assertEquals("어린 왕자", results.getContent().get(0).getTitle());
    }
}
