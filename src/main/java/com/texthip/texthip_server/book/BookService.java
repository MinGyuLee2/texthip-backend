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
import com.texthip.texthip_server.book.dto.AladinSearchResponseDto.AladinBookDto;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiClient aladinApiClient;

    // ISBN으로 특정 도서 정보 조회 
    @Transactional
    public BookDetailResponseDto getBookDetails(String isbn) {
        // 1. DB에서 ISBN으로 책을 찾습니다.
        Book book = bookRepository.findById(isbn)
                // 2. DB에 책이 없으면 orElseGet 블록을 실행합니다.
                .orElseGet(() -> {
                    // 3. 알라딘 API를 호출합니다.
                    AladinSearchResponseDto response = aladinApiClient.lookupBookByIsbn(isbn);

                    // 알라딘에서도 책을 찾지 못하면 예외를 발생시킵니다.
                    if (response.getItem() == null || response.getItem().isEmpty()) {
                        throw new BookNotFoundException(isbn);
                    }
                    
                    AladinBookDto aladinBookDto = response.getItem().get(0);

                    // 4. API 결과를 Book 엔티티로 변환합니다.
                    Book newBook = Book.builder()
                            .isbn(aladinBookDto.getIsbn13())
                            .title(aladinBookDto.getTitle())
                            .author(aladinBookDto.getAuthor())
                            .publisher(aladinBookDto.getPublisher())
                            .description(aladinBookDto.getDescription())
                            .coverImageUrl(aladinBookDto.getCover())
                            .publicationDate(LocalDate.parse(aladinBookDto.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                            .build();
                    
                    // 5. DB에 저장하고, 저장된 엔티티를 반환합니다.
                    return bookRepository.save(newBook);
                });

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

    // 베스트셀러 목록 조회 및 DB 저장
    @Transactional
    public List<BookDetailResponseDto> getBestsellerList(int count) {
        // 1. 알라딘 API 호출
        AladinSearchResponseDto aladinResponse = aladinApiClient.getBestsellers(count);

        // 2. API 결과를 우리 Book 엔티티로 변환하고, 없으면 DB에 저장 (Upsert)
        List<Book> books = aladinResponse.getItem().stream()
                .map(aladinBookDto -> 
                    bookRepository.findById(aladinBookDto.getIsbn13())
                        .orElseGet(() -> {
                            Book newBook = Book.builder()
                                    .isbn(aladinBookDto.getIsbn13())
                                    .title(aladinBookDto.getTitle())
                                    .author(aladinBookDto.getAuthor())
                                    .publisher(aladinBookDto.getPublisher())
                                    .description(aladinBookDto.getDescription())
                                    .coverImageUrl(aladinBookDto.getCover())
                                    .publicationDate(LocalDate.parse(aladinBookDto.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                                    .build();
                            return bookRepository.save(newBook);
                        })
                )
                .collect(Collectors.toList());

        // 3. 최종 결과를 DTO 리스트로 변환하여 반환
        return books.stream()
                .map(BookDetailResponseDto::new)
                .collect(Collectors.toList());
    }
}