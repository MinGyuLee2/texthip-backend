package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.AladinSearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class AladinApiClient {

    private final RestTemplate restTemplate;

    @Value("${aladin.api.ttb-key}")
    private String ttbKey;

    @Value("${aladin.api.base-url}")
    private String baseUrl;

    public AladinApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 제목으로 책 검색
    public AladinSearchResponseDto searchBooksByTitle(String title) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemSearch.aspx") 
                .queryParam("TTBKey", ttbKey)
                .queryParam("Query", title)
                .queryParam("QueryType", "Title")
                .queryParam("MaxResults", 10)
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .build(true)
                .toUri();

        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }

    // ISBN으로 책 상세 정보 조회
    public AladinSearchResponseDto lookupBookByIsbn(String isbn) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/ItemLookUp.aspx")
                .queryParam("TTBKey", ttbKey)
                .queryParam("ItemId", isbn)
                .queryParam("ItemIdType", "ISBN13")
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .build(true)
                .toUri();

        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }

    // 베스트셀러 목록 조회
    public AladinSearchResponseDto getBestsellers(int count) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/ItemList.aspx")
                .queryParam("TTBKey", ttbKey)
                .queryParam("QueryType", "Bestseller")
                .queryParam("MaxResults", count) // 상위에서 count만큼만 가져오도록 설정
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .build(true)
                .toUri();

        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }
}