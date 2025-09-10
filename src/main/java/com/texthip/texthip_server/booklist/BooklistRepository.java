package com.texthip.texthip_server.booklist;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Booklist 엔티티에 대한 데이터베이스 연산을 처리하는 JpaRepository 인터페이스입니다.
 */
public interface BooklistRepository extends JpaRepository<Booklist, Long> {
}
