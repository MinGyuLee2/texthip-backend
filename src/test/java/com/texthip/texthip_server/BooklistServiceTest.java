package com.texthip.texthip_server;

import com.texthip.texthip_server.booklist.Booklist;
import com.texthip.texthip_server.booklist.BooklistRepository;
import com.texthip.texthip_server.booklist.BooklistService;
import com.texthip.texthip_server.book.Book;
import com.texthip.texthip_server.book.BookRepository;
import com.texthip.texthip_server.booklist.dto.BooklistCreateRequestDto;
import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BooklistServiceTest {

    @Autowired
    private BooklistService booklistService;

    @Autowired
    private BooklistRepository booklistRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Book testBook;
    private Booklist testBooklist;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자, 책, 북리스트 데이터 미리 생성
        testUser = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("tester")
                .build();
        userRepository.save(testUser);

        testBook = Book.builder()
                .isbn("9788937460731")
                .title("어린 왕자")
                .author("생텍쥐페리")
                .build();
        bookRepository.save(testBook);

        testBooklist = Booklist.builder()
                .title("나의 인생 책 목록")
                .user(testUser)
                .build();
        booklistRepository.save(testBooklist);
    }

    @Test
    @DisplayName("북리스트 생성 성공")
    void createBooklist_success() {
        // given
        BooklistCreateRequestDto requestDto = new BooklistCreateRequestDto("새로운 북리스트", "설명");
        String userEmail = testUser.getEmail();

        // when
        booklistService.createBooklist(requestDto, userEmail);

        // then
        Booklist foundBooklist = booklistRepository.findAll().stream()
                .filter(b -> b.getTitle().equals("새로운 북리스트"))
                .findFirst()
                .orElse(null);

        assertNotNull(foundBooklist);
        assertEquals("새로운 북리스트", foundBooklist.getTitle());
        assertEquals(testUser.getId(), foundBooklist.getUser().getId());
    }

    @Test
    @DisplayName("북리스트에 책 추가 성공")
    void addBookToBooklist_success() throws AccessDeniedException {
        // given
        Long booklistId = testBooklist.getId();
        String isbn = testBook.getIsbn();
        String userEmail = testUser.getEmail();

        // when
        booklistService.addBookToBooklist(booklistId, isbn, userEmail);

        // then
        Booklist updatedBooklist = booklistRepository.findById(booklistId).get();
        assertEquals(1, updatedBooklist.getItems().size());
        assertEquals(isbn, updatedBooklist.getItems().get(0).getBook().getIsbn());
    }

    @Test
    @DisplayName("권한 없는 북리스트에 책 추가 시 예외 발생")
    void addBookToBooklist_fail_unauthorized() {
        // given
        // 다른 사용자 생성
        User anotherUser = User.builder()
                .email("another@example.com")
                .password("password123")
                .nickname("another")
                .build();
        userRepository.save(anotherUser);

        Long booklistId = testBooklist.getId(); // testUser의 북리스트
        String isbn = testBook.getIsbn();
        String anotherUserEmail = anotherUser.getEmail(); // 다른 사용자의 이메일로 시도

        // when & then
        assertThrows(AccessDeniedException.class, () -> {
            booklistService.addBookToBooklist(booklistId, isbn, anotherUserEmail);
        });
    }
}