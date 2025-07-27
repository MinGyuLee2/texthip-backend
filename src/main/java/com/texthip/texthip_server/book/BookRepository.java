package com.texthip.texthip_server.book;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    // 도서 제목으로 책을 검색하는 기능 (대소문자 무시)
    List<Book> findByTitleContainingIgnoreCase(String title);
}
