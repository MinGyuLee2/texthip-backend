package com.texthip.texthip_server.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchRequestDto {
    private String query;
    private int page = 0; // 기본 페이지 0
    private int size = 10; // 기본 사이즈 10
}