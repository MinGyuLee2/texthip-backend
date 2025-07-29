package com.texthip.texthip_server.booklist;

import com.texthip.texthip_server.book.Book;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "booklist_items")
public class BooklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booklist_id", nullable = false)
    private Booklist booklist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @Builder
    public BooklistItem(Booklist booklist, Book book) {
        this.booklist = booklist;
        this.book = book;
    }
}