package com.texthip.texthip_server.book.dto;

import com.texthip.texthip_server.book.Book;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 도서 상세 정보를 클라이언트에게 응답으로 전달하기 위한 DTO 클래스입니다.
 */
@Getter
public class BookDetailResponseDto {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String coverImageUrl;
    private LocalDate publicationDate;
    private Integer pageCount;
    private Integer bookWidth;
    private Integer bookHeight;
    private Integer bookDepth;

    /**
     * Book 엔티티를 BookDetailResponseDto로 변환하는 생성자입니다.
     * 엔티티 객체를 직접 노출하지 않고, 필요한 데이터만 가공하여 전달하는 역할을 합니다.
     * @param book 변환할 Book 엔티티 객체
     */
    public BookDetailResponseDto(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.description = book.getDescription();
        this.coverImageUrl = book.getCoverImageUrl();
        this.publicationDate = book.getPublicationDate();
        this.pageCount = book.getPageCount();
        this.bookWidth = book.getBookWidth();
        this.bookHeight = book.getBookHeight();
        this.bookDepth = book.getBookDepth();
    }
}
