package com.texthip.texthip_server.book;

import com.texthip.texthip_server.book.dto.AladinSearchResponseDto;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 외부 알라딘 Open API와 통신하는 역할을 담당하는 클라이언트 클래스입니다.
 * RestTemplate을 사용하여 HTTP 요청을 보냅니다.
 */
@Component
@RequiredArgsConstructor
public class AladinApiClient {

    private final RestTemplate restTemplate;

    // application.yml에 설정된 알라딘 TTB 키
    @Value("${aladin.api.ttb-key}")
    private String ttbKey;

    // application.yml에 설정된 알라딘 API 기본 URL
    @Value("${aladin.api.base-url}")
    private String baseUrl;

     /**
     * 상품 조회 API를 호출하여 ISBN으로 특정 도서의 상세 정보를 조회합니다.
     * @param isbn 조회할 도서의 13자리 ISBN
     * @return 알라딘 API 응답 DTO
     */
    public AladinSearchResponseDto lookupBookByIsbn(String isbn) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemLookUp.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("itemIdType", "ISBN13")
                .queryParam("ItemId", isbn)
                .queryParam("output", "js") // 응답 형식을 JSON으로 지정
                .queryParam("Version", "20131101")
                .queryParam("OptResult", "fulldescription,packing") // 상세 설명까지 모두 포함하도록 요청
                .encode(StandardCharsets.UTF_8) // 한글 등 파라미터가 깨지지 않도록 UTF-8로 인코딩
                .build()
                .toUri();
        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }

    /**
     * 상품 검색 API를 호출하여 키워드로 도서를 검색합니다.
     * @param keyword 검색어
     * @param start 검색 시작 페이지 번호 (1부터 시작)
     * @return 알라딘 API 응답 DTO
     */
    public AladinSearchResponseDto searchBooksByKeyword(String keyword, int start) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemSearch.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("Query", keyword)
                .queryParam("QueryType", "Keyword") // 검색 종류를 키워드로 지정
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
     * 상품 리스트 API를 호출하여 베스트셀러 목록을 조회합니다.
     * @param size 조회할 베스트셀러 개수
     * @return 알라딘 API 응답 DTO
     */
    public AladinSearchResponseDto getBestsellers(int size) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl + "/ItemList.aspx")
                .queryParam("ttbkey", ttbKey)
                .queryParam("QueryType", "Bestseller") // 리스트 종류를 베스트셀러로 지정
                .queryParam("MaxResults", size)
                .queryParam("start", 1)
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "js")
                .queryParam("Version", "20131101")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, AladinSearchResponseDto.class);
    }
}
