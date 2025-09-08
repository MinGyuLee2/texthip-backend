package com.texthip.texthip_server.book;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Book 엔티티에 대한 데이터베이스 연산을 처리하는 JpaRepository 인터페이스입니다.
 */
public interface BookRepository extends JpaRepository<Book, String> {

    /**
     * 도서 제목에 특정 키워드가 포함된 책을 검색합니다. (대소문자 무시)
     * 페이징 기능을 지원합니다.
     * @param title 검색할 키워드
     * @param pageable 페이징 정보
     * @return 페이징된 Book 엔티티 목록
     */
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

     /**
     * 주어진 ISBN 목록 중에서 DB에 이미 존재하는 ISBN들만 조회합니다.
      * BookService의 upsert 로직에서 N+1 문제를 방지하기 위해 사용됩니다.
     * @param isbns 확인할 ISBN 목록
     * @return DB에 이미 존재하는 ISBN 집합
     */
    @Query("SELECT b.isbn FROM Book b WHERE b.isbn IN :isbns")
    Set<String> findExistingIsbns(@Param("isbns") Set<String> isbns);
}
