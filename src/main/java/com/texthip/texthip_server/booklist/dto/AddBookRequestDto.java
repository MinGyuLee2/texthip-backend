package com.texthip.texthip_server.booklist.dto;

import lombok.Getter;

/**
 * 북리스트에 책을 추가하는 API(POST /api/booklists/{id}/books) 요청 시,
 * 클라이언트로부터 추가할 책의 ISBN을 전달받기 위한 DTO입니다.
 */
@Getter
public class AddBookRequestDto {
    private String isbn;
}
