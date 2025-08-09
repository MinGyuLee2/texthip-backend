package com.texthip.texthip_server.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
    // 도서 제목으로 책을 검색하는 기능 (대소문자 무시)
    // 페이징 기능을 지원하도록 Pageable 파라미터를 추가하고 Page 객체를 반환합니다.
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
