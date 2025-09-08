package com.texthip.texthip_server.book;

/**
 * DB나 외부 API에서 특정 ISBN의 책을 찾을 수 없을 때 발생하는 커스텀 예외 클래스입니다.
 * RuntimeException을 상속받아, 명시적인 예외 처리를 강제하지 않습니다.
 */
public class BookNotFoundException extends RuntimeException {
    /**
     * 예외를 생성할 때, 찾지 못한 ISBN 정보를 포함한 메시지를 설정합니다.
     * @param isbn 찾지 못한 책의 ISBN
     */
    public BookNotFoundException(String isbn) {
        super("해당 ISBN의 책을 찾을 수 없습니다: " + isbn);
    }
}
