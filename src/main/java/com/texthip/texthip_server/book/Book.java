package com.texthip.texthip_server.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor 
@Builder 
@Table(name = "books")
public class Book {

    @Id
    @Column(length = 13)
    private String isbn;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(length = 2000)
    private String description;

    @Column(length = 255)
    private String coverImageUrl;

    private LocalDate publicationDate;

    private Integer pageCount;
}
