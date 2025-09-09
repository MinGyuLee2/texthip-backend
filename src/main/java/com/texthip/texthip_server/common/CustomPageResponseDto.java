package com.texthip.texthip_server.common;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

/**
 * Spring Data JPA의 Page 객체를 클라이언트 친화적인 JSON 형식으로 변환하기 위한 커스텀 DTO 클래스입니다.
 * 불필요한 페이징 정보를 제외하고, 프론트엔드에서 사용하기 쉬운 정보들만 담아 전달합니다.
 *
 * @param <T> 페이지에 포함된 데이터의 타입
 */
@Getter
public class CustomPageResponseDto<T> {
    private final List<T> content;          // 현재 페이지의 데이터 목록
    private final int totalPages;           // 전체 페이지 수
    private final long totalElements;       // 전체 데이터 개수
    private final int currentPage;          // 현재 페이지 번호 (0부터 시작)
    private final boolean isLast;           // 마지막 페이지 여부

    /**
     * Page 객체를 CustomPageResponseDto로 변환하는 생성자입니다.
     * @param page 변환할 Spring Data JPA의 Page 객체
     */
    public CustomPageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.isLast = page.isLast();
    }
}
