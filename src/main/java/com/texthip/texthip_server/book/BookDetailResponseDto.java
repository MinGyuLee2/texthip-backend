package com.texthip.texthip_server.book;

import lombok.Getter;

import java.time.LocalDate;

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