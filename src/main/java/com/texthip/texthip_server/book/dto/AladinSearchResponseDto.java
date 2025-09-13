package com.texthip.texthip_server.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
        private SubInfo subInfo;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SubInfo {
            // subInfo 객체 안에 packing 객체가 포함됨
            private PackingInfo packing;
        }

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PackingInfo {
            private int sizeWidth;  // 책 너비(가로)
            private int sizeHeight; // 책 높이(세로)
            private int sizeDepth;  // 책 깊이(두께)
        }
    }
}

