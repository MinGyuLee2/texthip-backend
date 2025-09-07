package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.AladinSearchResponseDto;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AladinApiClient {

    private final RestTemplate restTemplate;

    // application-local.yml 파일의 ttb-key 값을 주입받도록 수정
    @Value("${aladin.api.ttb-key}")
    private String ttbKey;

    // application-local.yml 파일의 base-url 값을 주입받도록 수정
    @Value("${aladin.api.base-url}")
    private String baseUrl;

     /**
     * ISBN으로 특정 도서의 상세 정보를 조회합니다.
     */
    public AladinSearchResponseDto lookupBookByIsbn(String isbn) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemLookUp.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("itemIdType", "ISBN13")
                .queryParam("ItemId", isbn)
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .queryParam("OptResult", "fulldescription")
                .build(true) 
                .toUri();
        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }

    /**
     * 키워드로 도서를 검색합니다.
     */
    public AladinSearchResponseDto searchBooksByKeyword(String keyword, int start) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemSearch.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("Query", keyword)
                .queryParam("QueryType", "Keyword")
                .queryParam("MaxResults", 20)
                .queryParam("start", start)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .encode(StandardCharsets.UTF_8) 
                .build()                       
                .toUri();
        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }
    
    /**
     * 베스트셀러 목록을 조회합니다.
     */
    public AladinSearchResponseDto getBestsellers(int size) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemList.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("QueryType", "Bestseller")
                .queryParam("MaxResults", size)
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .build(true) 
                .toUri();
        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }
}