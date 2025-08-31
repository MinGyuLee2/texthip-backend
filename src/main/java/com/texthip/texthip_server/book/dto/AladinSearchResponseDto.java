package com.texthip.texthip_server.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알라딘 응답의 모든 필드를 매핑할 필요 없도록 설정
public class AladinSearchResponseDto {

    private List<AladinBookDto> item;

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