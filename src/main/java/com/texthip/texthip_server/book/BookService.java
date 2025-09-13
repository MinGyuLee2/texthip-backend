package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.AladinSearchResponseDto;
import com.texthip.texthip_server.book.dto.BookDetailResponseDto;
import com.texthip.texthip_server.common.CustomPageResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 도서 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * (도서 검색, 상세 정보 조회, 베스트셀러 조회 등)
 * 외부 알라딘 API와 통신하며, 조회된 데이터를 내부 DB에 캐싱하는 역할을 합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiClient aladinApiClient;

    /**
     * ISBN으로 특정 도서의 상세 정보를 조회합니다.
     * 1. 내부 DB에서 먼저 조회합니다.
     * 2. DB에 없으면 알라딘 API를 호출하여 정보를 가져온 후, DB에 저장(캐싱)하고 반환합니다.
     *
     * @param isbn 조회할 도서의 13자리 ISBN
     * @return 도서 상세 정보 DTO
     * @throws BookNotFoundException 알라딘 API에서도 책을 찾지 못한 경우
     */
    @Transactional
    public BookDetailResponseDto getBookDetails(String isbn) {
        Book book = bookRepository.findById(isbn)
                .orElseGet(() -> {
                    // DB에 책이 없는 경우, 알라딘 API를 호출
                    AladinSearchResponseDto response = aladinApiClient.lookupBookByIsbn(isbn);
                    if (response == null || response.getItem() == null || response.getItem().isEmpty()) {
                        throw new BookNotFoundException(isbn);
                    }
                    // API 응답을 Book 엔티티로 변환하여 DB에 저장
                    return bookRepository.save(convertDtoToBook(response.getItem().get(0)));
                });
        return new BookDetailResponseDto(book);
    }


    /**
     * 알라딘 API를 통해 베스트셀러 목록을 조회하고, 해당 도서 정보를 DB에 업데이트(Upsert)합니다.
     *
     * @param size 조회할 베스트셀러의 개수
     * @return 베스트셀러 도서 목록 DTO
     */
     @Transactional
    public List<BookDetailResponseDto> getBestsellerList(int size) {
        AladinSearchResponseDto aladinResponse = aladinApiClient.getBestsellers(size);

        if (aladinResponse == null || aladinResponse.getItem() == null) {
            return Collections.emptyList();
        }

        // 알라딘 API 결과를 DB에 저장 또는 업데이트
        upsertBooksFromAladin(aladinResponse.getItem());

        // API 응답에 포함된 ISBN 목록을 추출
        List<String> isbns = aladinResponse.getItem().stream()
                .map(AladinSearchResponseDto.AladinBookDto::getIsbn13)
                .collect(Collectors.toList());

        // 최종적으로 DB에서 해당 책 정보를 조회하여 반환
        return bookRepository.findAllById(isbns).stream()
                .map(BookDetailResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 알라딘 API 응답 목록(AladinBookDto 리스트)을 받아, DB에 존재하지 않는 책만 저장합니다. (성능 최적화)
     * N+1 문제를 방지하기 위해, 모든 책의 존재 여부를 한 번의 쿼리로 확인하고, 없는 책들만 한 번에 저장합니다.
     *
     * @param aladinBooks 알라딘 API에서 받아온 도서 정보 리스트
     */
    private void upsertBooksFromAladin(List<AladinSearchResponseDto.AladinBookDto> aladinBooks) {
        // 1. API 결과에서 ISBN 목록을 추출
        Set<String> isbns = aladinBooks.stream()
                .map(AladinSearchResponseDto.AladinBookDto::getIsbn13)
                .filter(isbn -> isbn != null && !isbn.isEmpty()) // ISBN이 없는 경우 필터링
                .collect(Collectors.toSet());
        
        if (isbns.isEmpty()) return;

        // 2. 한 번의 쿼리로 DB에 이미 존재하는 ISBN 목록을 조회
        Set<String> existingIsbns = bookRepository.findExistingIsbns(isbns);

        // 3. API 결과 중, DB에 존재하지 않는 책들만 필터링하여 Book 엔티티 리스트로 변환
        List<Book> newBooks = aladinBooks.stream()
                .filter(dto -> dto.getIsbn13() != null && !dto.getIsbn13().isEmpty() && !existingIsbns.contains(dto.getIsbn13()))
                .map(this::convertDtoToBook)
                .collect(Collectors.toList());

        // 4. 새로운 책이 있을 경우, 한 번의 쿼리로 모두 저장 (Bulk Insert)
        if (!newBooks.isEmpty()) {
            bookRepository.saveAll(newBooks);
        }
    }

     /**
     * 키워드로 도서를 검색합니다.
     * 1. 알라딘 API를 호출하여 최신 검색 결과를 가져옵니다.
     * 2. API 결과를 내부 DB에 Upsert하여 데이터를 최신 상태로 유지합니다.
     * 3. 최종적으로 내부 DB에서 키워드로 다시 검색하여 페이징된 결과를 반환합니다.
     *
     * @param keyword 검색어
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기)
     * @return 페이징된 도서 검색 결과 DTO
     */
    @Transactional
    public CustomPageResponseDto<BookDetailResponseDto> searchBooksByKeyword(String keyword, Pageable pageable) {
        // 알라딘 API는 페이지 번호가 1부터 시작하므로, 클라이언트에서 받은 페이지 번호(0부터 시작)에 1을 더해줍니다.
        AladinSearchResponseDto aladinResponse = aladinApiClient.searchBooksByKeyword(keyword, pageable.getPageNumber() + 1);

        // 알라딘 API 검색 결과가 없는 경우, DB에서만 검색하여 반환
        if (aladinResponse == null || aladinResponse.getItem() == null || aladinResponse.getItem().isEmpty()) {
            Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
            return new CustomPageResponseDto<>(bookPage.map(BookDetailResponseDto::new));
        }

        // API 결과를 DB에 저장 또는 업데이트
        upsertBooksFromAladin(aladinResponse.getItem());

        // API 호출로 최신화된 DB에서 다시 검색하여 페이징된 결과를 반환
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        Page<BookDetailResponseDto> bookDtoPage = bookPage.map(BookDetailResponseDto::new);

        return new CustomPageResponseDto<>(bookDtoPage);
    }

    /**
     * AladinBookDto를 Book 엔티티로 변환하는 공통 메소드입니다.
     * 날짜 파싱 중 발생할 수 있는 예외를 처리하여 안정성을 높였습니다.
     *
     * @param dto 알라딘 API 응답 DTO
     * @return Book 엔티티
     */
    private Book convertDtoToBook(AladinSearchResponseDto.AladinBookDto dto) {
        LocalDate publicationDate = null;
        try {
            // 알라딘 API가 다양한 날짜 형식을 반환할 수 있으므로, 유효한 경우에만 파싱 시도
            if (dto.getPubDate() != null && !dto.getPubDate().isEmpty()) {
                 publicationDate = LocalDate.parse(dto.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            }
        } catch (Exception e) {
            // 날짜 형식이 맞지 않아 파싱에 실패할 경우, 로그를 남기고 해당 필드는 null로 처리
            log.error("날짜 파싱 오류: ISBN={}, PubDate={}", dto.getIsbn13(), dto.getPubDate(), e);
        }
        
        Book.BookBuilder bookBuilder = Book.builder()
                .isbn(dto.getIsbn13())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .description(dto.getDescription())
                .coverImageUrl(dto.getCover())
                .publicationDate(publicationDate);

        if (dto.getSubInfo() != null && dto.getSubInfo().getPacking() != null) {
            AladinSearchResponseDto.AladinBookDto.PackingInfo packingInfo = dto.getSubInfo().getPacking();
            bookBuilder.bookWidth(packingInfo.getSizeWidth());
            bookBuilder.bookHeight(packingInfo.getSizeHeight());
            bookBuilder.bookDepth(packingInfo.getSizeDepth());
        }
        
        return bookBuilder.build();
    }
}

