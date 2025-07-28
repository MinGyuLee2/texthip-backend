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

@Service
@RequiredArgsConstructor
public class BooklistService {

    private final BooklistRepository booklistRepository;
    private final BooklistItemRepository booklistItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // 새 북리스트 생성
    @Transactional
    public void createBooklist(BooklistCreateRequestDto requestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Booklist booklist = Booklist.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .user(user)
                .build();
        
        booklistRepository.save(booklist);
    }

    // 북리스트에 책 추가
    @Transactional
    public void addBookToBooklist(Long booklistId, String isbn, String userEmail) throws AccessDeniedException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        
        Booklist booklist = booklistRepository.findById(booklistId)
                .orElseThrow(() -> new NoSuchElementException("북리스트를 찾을 수 없습니다."));

        // 북리스트 소유권 확인
        if (!booklist.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("북리스트에 대한 권한이 없습니다.");
        }
        
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new NoSuchElementException("책을 찾을 수 없습니다."));

        BooklistItem booklistItem = BooklistItem.builder()
                .booklist(booklist)
                .book(book)
                .build();
        
        booklistItemRepository.save(booklistItem);
    }
}