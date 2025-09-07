package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.AladinSearchResponseDto;
import com.texthip.texthip_server.book.dto.BookDetailResponseDto;
import com.texthip.texthip_server.common.CustomPageResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiClient aladinApiClient;

    /**
     * ISBN으로 특정 도서의 상세 정보를 조회합니다.
     * DB에 없으면 알라딘 API를 호출하여 정보를 가져온 후 DB에 저장(캐싱)합니다.
     */
    @Transactional
    public BookDetailResponseDto getBookDetails(String isbn) {
        Book book = bookRepository.findById(isbn)
                .orElseGet(() -> {
                    AladinSearchResponseDto response = aladinApiClient.lookupBookByIsbn(isbn);
                    if (response == null || response.getItem() == null || response.getItem().isEmpty()) {
                        throw new BookNotFoundException(isbn);
                    }
                    return bookRepository.save(convertDtoToBook(response.getItem().get(0)));
                });
        return new BookDetailResponseDto(book);
    }


    /**
     * 알라딘 API를 통해 베스트셀러 목록을 조회하고 DB에 업데이트합니다.
     */
     @Transactional
    public List<BookDetailResponseDto> getBestsellerList(int size) {
        AladinSearchResponseDto aladinResponse = aladinApiClient.getBestsellers(size);

        if (aladinResponse == null || aladinResponse.getItem() == null) {
            return Collections.emptyList();
        }

        upsertBooksFromAladin(aladinResponse.getItem());

        List<String> isbns = aladinResponse.getItem().stream()
                .map(AladinSearchResponseDto.AladinBookDto::getIsbn13)
                .collect(Collectors.toList());

        return bookRepository.findAllById(isbns).stream()
                .map(BookDetailResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 알라딘 API 응답 목록을 받아, DB에 존재하지 않는 책만 저장합니다. (성능 최적화)
     */
    private void upsertBooksFromAladin(List<AladinSearchResponseDto.AladinBookDto> aladinBooks) {
        Set<String> isbns = aladinBooks.stream()
                .map(AladinSearchResponseDto.AladinBookDto::getIsbn13)
                .collect(Collectors.toSet());

        Set<String> existingIsbns = bookRepository.findExistingIsbns(isbns);

        List<Book> newBooks = aladinBooks.stream()
                .filter(dto -> !existingIsbns.contains(dto.getIsbn13()))
                .map(this::convertDtoToBook)
                .collect(Collectors.toList());

        if (!newBooks.isEmpty()) {
            bookRepository.saveAll(newBooks);
        }
    }

     /**
     * 키워드로 도서를 검색합니다.
     * 알라딘 API에서 검색 결과를 가져와 DB를 업데이트하고, 최종적으로 DB에서 페이징된 결과를 반환합니다.
     */
    @Transactional
    public CustomPageResponseDto<BookDetailResponseDto> searchBooksByKeyword(String keyword, Pageable pageable) {
        // 알라딘 API는 페이지 번호가 1부터 시작하므로 +1
        AladinSearchResponseDto aladinResponse = aladinApiClient.searchBooksByKeyword(keyword, pageable.getPageNumber() + 1);

        if (aladinResponse == null || aladinResponse.getItem() == null || aladinResponse.getItem().isEmpty()) {
            // 알라딘 검색 결과가 없으면 DB에서만 검색
            Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
            return new CustomPageResponseDto<>(bookPage.map(BookDetailResponseDto::new));
        }

        // API 결과를 DB에 저장 또는 업데이트 (Upsert)
        upsertBooksFromAladin(aladinResponse.getItem());

        // DB에서 최종적으로 다시 검색하여 페이징된 결과 반환
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        Page<BookDetailResponseDto> bookDtoPage = bookPage.map(BookDetailResponseDto::new);

        return new CustomPageResponseDto<>(bookDtoPage);
    }

    /**
     * AladinBookDto를 Book 엔티티로 변환하는 공통 메소드
     */
    private Book convertDtoToBook(AladinSearchResponseDto.AladinBookDto dto) {
        return Book.builder()
                .isbn(dto.getIsbn13())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .description(dto.getDescription())
                .coverImageUrl(dto.getCover())
                .publicationDate(LocalDate.parse(dto.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                .build();
    }
}