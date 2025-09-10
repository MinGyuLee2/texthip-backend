package com.texthip.texthip_server.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서 검색 API(POST /api/books/search) 요청 시,
 * 클라이언트로부터 검색어와 페이징 정보를 전달받기 위한 DTO입니다.
 */
@Getter
@NoArgsConstructor
public class SearchRequestDto {
    // 검색할 키워드
    private String query;
    // 조회할 페이지 번호 (0부터 시작)
    private int page = 0;
    // 한 페이지에 보여줄 결과 개수
    private int size = 10;
}
