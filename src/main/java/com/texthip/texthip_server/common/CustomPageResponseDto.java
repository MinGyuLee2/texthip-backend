package com.texthip.texthip_server.common;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
public class CustomPageResponseDto<T> {
    private final List<T> content;          // 데이터 목록
    private final int totalPages;           // 전체 페이지 수
    private final long totalElements;       // 전체 데이터 개수
    private final int currentPage;          // 현재 페이지 번호 (0부터 시작)
    private final boolean isLast;           // 마지막 페이지 여부

    /**
     * Page 객체를 우리가 원하는 DTO 형태로 변환하는 생성자.
     * @param page Spring Data JPA의 Page 객체
     */
    public CustomPageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.isLast = page.isLast();
    }
}