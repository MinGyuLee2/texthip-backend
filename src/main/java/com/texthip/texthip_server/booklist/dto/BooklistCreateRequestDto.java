package com.texthip.texthip_server.booklist.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 북리스트 생성 API(POST /api/booklists) 요청 시,
 * 클라이언트로부터 제목과 설명을 전달받기 위한 DTO입니다.
 */
@Getter
@AllArgsConstructor
public class BooklistCreateRequestDto {
    private String title;
    private String description;
}
