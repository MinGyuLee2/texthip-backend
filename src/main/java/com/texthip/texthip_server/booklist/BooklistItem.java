package com.texthip.texthip_server.booklist;

import com.texthip.texthip_server.book.Book;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 북리스트와 책 사이의 관계를 나타내는 엔티티입니다. (매핑 테이블 역할)
 * 'booklist_items' 테이블과 매핑됩니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "booklist_items")
public class BooklistItem {

    /**
     * 북리스트 아이템의 고유 식별자입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이 아이템이 속한 북리스트입니다.
     * Booklist 엔티티와 다대일(N:1) 관계입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booklist_id", nullable = false)
    private Booklist booklist;

    /**
     * 이 아이템이 가리키는 책입니다.
     * Book 엔티티와 다대일(N:1) 관계입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @Builder
    public BooklistItem(Booklist booklist, Book book) {
        this.booklist = booklist;
        this.book = book;
    }
}
