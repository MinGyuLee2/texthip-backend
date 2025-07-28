package com.texthip.texthip_server.booklist.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class BooklistCreateRequestDto {
    private String title;
    private String description;
}