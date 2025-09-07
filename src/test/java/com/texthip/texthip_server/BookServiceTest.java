package com.texthip.texthip_server;

import com.texthip.texthip_server.book.Book;
import com.texthip.texthip_server.book.BookNotFoundException;
import com.texthip.texthip_server.book.BookRepository;
import com.texthip.texthip_server.book.BookService;
import com.texthip.texthip_server.book.dto.BookDetailResponseDto;
import com.texthip.texthip_server.common.CustomPageResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        testBook = Book.builder()
                .isbn("9788937460731")
                .title("어린 왕자")
                .author("앙투안 드 생텍쥐페리")
                .publisher("민음사")
                .build();
        bookRepository.save(testBook);
    }

    @Test
    @DisplayName("키워드로 도서 검색 성공")
    void searchBooksByKeyword_success() {
        // given
        String keyword = "자바";
        Pageable pageable = PageRequest.of(0, 5);

        // when
        CustomPageResponseDto<BookDetailResponseDto> result = bookService.searchBooksByKeyword(keyword, pageable);

        // then
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertTrue(result.getTotalElements() > 0);
        assertEquals(0, result.getCurrentPage());
    }

    @Test
    @DisplayName("DB에 책이 있을 때 ISBN으로 상세 정보 조회 성공")
    void getBookDetails_success_whenBookExistsInDb() {
        // given
        String isbn = "9788937460731"; 

        // when
        BookDetailResponseDto responseDto = bookService.getBookDetails(isbn);

        // then
        assertNotNull(responseDto);
        assertEquals(isbn, responseDto.getIsbn());
        assertEquals("어린 왕자", responseDto.getTitle());
    }

    @Test
    @DisplayName("DB에 책이 없을 때 ISBN으로 상세 정보 조회 성공")
    void getBookDetails_success_whenBookNotInDb() {
        // given
        String isbn = "9788954442695"; 

        // when
        BookDetailResponseDto responseDto = bookService.getBookDetails(isbn);

        // then
        assertNotNull(responseDto);
        assertEquals(isbn, responseDto.getIsbn());
        assertEquals("삼체 1부 : 삼체문제", responseDto.getTitle()); 
        assertNotNull(responseDto.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 ISBN으로 조회 시 예외 발생")
    void getBookDetails_fail_with_non_existent_isbn() {
        // given
        String nonExistentIsbn = "9791199999999";

        // when & then
        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookDetails(nonExistentIsbn);
        });
    }

    @Test
    @DisplayName("베스트셀러 목록 조회 성공")
    void getBestsellerList_success() {
        // given
        int size = 3;

        // when
        List<BookDetailResponseDto> bestsellers = bookService.getBestsellerList(size);

        // then
        assertNotNull(bestsellers);
        assertEquals(size, bestsellers.size());
    }
}