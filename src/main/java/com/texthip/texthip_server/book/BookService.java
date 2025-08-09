package com.texthip.texthip_server.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // ISBN으로 특정 도서 정보 조회
    @Transactional(readOnly = true)
    public BookDetailResponseDto getBookDetails(String isbn) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
        return new BookDetailResponseDto(book);
    }

    // 제목으로 도서 검색
    @Transactional(readOnly = true)
    public Page<BookDetailResponseDto> searchBooksByTitle(String title, Pageable pageable) {
        // TODO: 향후 외부 도서 API(알라딘 등)와 연동하여 DB에 없는 책도 검색하고 저장하는 로직 추가
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        return bookPage.map(BookDetailResponseDto::new);
    }
}