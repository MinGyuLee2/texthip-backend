package com.texthip.texthip_server.book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("해당 ISBN의 책을 찾을 수 없습니다: " + isbn);
    }
}

