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
}