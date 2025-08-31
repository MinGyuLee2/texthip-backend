package com.texthip.texthip_server.book;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

     // 제목으로 도서 검색 (DB + 알라딘 API 연동 및 저장)
    @Transactional 
    public Page<BookDetailResponseDto> searchBooksByTitle(String title, Pageable pageable) {
        // 1. 우리 DB에서 먼저 검색
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);

        // 2. DB 검색 결과가 없으면, 알라딘 API를 통해 외부에서 검색
        if (bookPage.isEmpty()) {
            AladinSearchResponseDto aladinResponse = aladinApiClient.searchBooksByTitle(title);

            // 3. 알라딘 검색 결과를 우리 Book 엔티티로 변환하고 DB에 저장
            List<Book> newBooks = aladinResponse.getItem().stream()
                    .map(aladinBookDto -> {
                        // 이미 DB에 존재하는 책인지 ISBN으로 확인 (중복 저장 방지)
                        return bookRepository.findById(aladinBookDto.getIsbn13())
                                .orElseGet(() -> {
                                    // DTO를 Book 엔티티로 변환
                                    Book book = Book.builder()
                                            .isbn(aladinBookDto.getIsbn13())
                                            .title(aladinBookDto.getTitle())
                                            .author(aladinBookDto.getAuthor())
                                            .publisher(aladinBookDto.getPublisher())
                                            .description(aladinBookDto.getDescription())
                                            .coverImageUrl(aladinBookDto.getCover())
                                            // String을 LocalDate로 변환
                                            .publicationDate(LocalDate.parse(aladinBookDto.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                                            .build();
                                    return bookRepository.save(book);
                                });
                    })
                    .collect(Collectors.toList());

            // 4. 저장된 엔티티 리스트를 Page<BookDetailResponseDto> 형식으로 변환하여 반환
            List<BookDetailResponseDto> dtoList = newBooks.stream()
                    .map(BookDetailResponseDto::new)
                    .collect(Collectors.toList());
            
            return new PageImpl<>(dtoList, pageable, dtoList.size());
        }

        return bookPage.map(BookDetailResponseDto::new);
    }
}