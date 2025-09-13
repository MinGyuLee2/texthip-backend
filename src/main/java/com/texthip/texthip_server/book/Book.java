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

/**
 * 도서 정보를 나타내는 JPA 엔티티 클래스입니다.
 * 'books' 테이블과 매핑됩니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자를 필요로 합니다. PROTECTED로 안전하게 설정합니다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 (주로 빌더 패턴과 함께 사용됩니다).
@Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있게 합니다.
@Table(name = "books")
public class Book {

    /**
     * 도서의 고유 식별자인 ISBN (13자리) 입니다.
     * 이 필드는 테이블의 기본 키(Primary Key)로 사용됩니다.
     */
    @Id
    @Column(length = 13)
    private String isbn;

    /**
     * 도서의 제목입니다.
     */
    @Column(length = 255, nullable = false)
    private String title;

    /**
     * 도서의 저자입니다.
     */
    @Column(length = 255, nullable = false)
    private String author;

    /**
     * 도서의 출판사입니다.
     */
    @Column(length = 100)
    private String publisher;

    /**
     * 도서에 대한 설명입니다.
     */
    @Column(length = 2000)
    private String description;

    /**
     * 도서 표지 이미지의 URL입니다.
     */
    @Column(length = 255)
    private String coverImageUrl;

    /**
     * 도서의 출판일입니다.
     */
    private LocalDate publicationDate;

    /**
     * 도서의 페이지 수입니다.
     */
    private Integer pageCount;

    /**
     * 책의 너비 (mm)
     */
    private Integer bookWidth;
    /**
     * 책의 높이 (mm)
     */
    private Integer bookHeight;
    /**
     * 책의 깊이/두께 (mm)
     */
    private Integer bookDepth;
}
