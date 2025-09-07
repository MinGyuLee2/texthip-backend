package com.texthip.texthip_server.book.dto;

import com.texthip.texthip_server.book.Book;
import lombok.Getter;

import java.time.LocalDate;

// 도서 상세 페이지에 필요한 모든 정보(ISBN, 제목, 저자, 설명 등)를 담아 클라이언트에게 응답합니다.
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

    // Book 엔티티를 DTO로 변환하는 생성자
    public BookDetailResponseDto(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.description = book.getDescription();
        this.coverImageUrl = book.getCoverImageUrl();
        this.publicationDate = book.getPublicationDate();
        this.pageCount = book.getPageCount();
    }
}