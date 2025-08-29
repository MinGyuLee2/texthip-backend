package com.texthip.texthip_server.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.texthip.texthip_server.book.dto.AladinSearchResponseDto;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiClient aladinApiClient;

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
        // 1. 먼저 우리 DB에서 검색
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);

         // 2. DB 검색 결과가 없으면, 알라딘 API를 통해 외부에서 검색
        if (bookPage.isEmpty()) {
            AladinSearchResponseDto aladinResponse = aladinApiClient.searchBooksByTitle(title);
            
            // TODO: 알라딘 검색 결과를 우리 Book 엔티티로 변환하고,
            //       필요하다면 우리 DB에 저장하는 로직 추가
            //       (이후 사용자들이 북리스트에 추가하거나 리뷰를 남기려면 DB에 존재해야 함)
            //       변환된 결과를 Page<BookDetailResponseDto> 형식으로 맞춰서 반환
        }

        return bookPage.map(BookDetailResponseDto::new);
    }
}