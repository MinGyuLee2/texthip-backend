package com.texthip.texthip_server.booklist;

import com.texthip.texthip_server.book.Book;
import com.texthip.texthip_server.book.BookRepository;
import com.texthip.texthip_server.booklist.dto.BooklistCreateRequestDto;
import com.texthip.texthip_server.user.User;
import com.texthip.texthip_server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

/**
 * 북리스트 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class BooklistService {

    private final BooklistRepository booklistRepository;
    private final BooklistItemRepository booklistItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * 새로운 북리스트를 생성합니다.
     * @param requestDto 북리스트 생성 요청 DTO
     * @param userEmail 북리스트를 생성하는 사용자의 이메일
     */
    @Transactional
    public void createBooklist(BooklistCreateRequestDto requestDto, String userEmail) {
        // 1. 이메일로 사용자 엔티티를 조회합니다.
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // 2. DTO와 사용자 정보를 바탕으로 Booklist 엔티티를 생성합니다.
        Booklist booklist = Booklist.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .user(user)
                .build();
        
        // 3. 생성된 북리스트를 DB에 저장합니다.
        booklistRepository.save(booklist);
    }

    /**
     * 특정 북리스트에 책을 추가합니다.
     * @param booklistId 책을 추가할 북리스트의 ID
     * @param isbn 추가할 책의 ISBN
     * @param userEmail 요청을 보낸 사용자의 이메일
     * @throws AccessDeniedException 북리스트의 소유자가 아닌 사용자가 추가를 시도할 경우
     */
    @Transactional
    public void addBookToBooklist(Long booklistId, String isbn, String userEmail) throws AccessDeniedException {
        // 1. 요청을 보낸 사용자의 정보를 조회합니다.
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        
        // 2. 책을 추가할 북리스트 정보를 조회합니다.
        Booklist booklist = booklistRepository.findById(booklistId)
                .orElseThrow(() -> new NoSuchElementException("북리스트를 찾을 수 없습니다."));

        // 3. 북리스트의 소유자가 현재 사용자가 맞는지 확인합니다.
        if (!booklist.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("북리스트에 대한 권한이 없습니다.");
        }
        
        // 4. 추가할 책의 정보를 조회합니다.
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        // 5. 북리스트와 책을 연결하는 BooklistItem 엔티티를 생성합니다.
        BooklistItem booklistItem = BooklistItem.builder()
                .booklist(booklist)
                .book(book)
                .build();
        
        // 6. 생성된 아이템을 DB에 저장하고, 북리스트의 items 리스트에도 추가합니다.
        booklistItemRepository.save(booklistItem);
        booklist.getItems().add(booklistItem);
    }
}
