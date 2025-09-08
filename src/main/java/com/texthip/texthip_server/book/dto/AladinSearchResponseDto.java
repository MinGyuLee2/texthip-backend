package com.texthip.texthip_server.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 알라딘 API의 응답 JSON을 Java 객체로 변환(역직렬화)하기 위한 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 응답 JSON의 모든 필드를 매핑할 필요 없이, 이 클래스에 정의된 필드만 매핑하도록 설정
public class AladinSearchResponseDto {

    // JSON 응답의 'item' 배열을 매핑할 필드
    private List<AladinBookDto> item;

    /**
     * 'item' 배열 내의 각 도서 객체를 매핑하기 위한 내부 정적 클래스입니다.
     */
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AladinBookDto {
        private String title;
        private String author;
        private String pubDate;
        private String description;
        private String isbn13;
        private String cover;
        private String publisher;
    }
}
