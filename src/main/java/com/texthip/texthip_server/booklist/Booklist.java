package com.texthip.texthip_server.booklist;

import com.texthip.texthip_server.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 사용자가 생성하는 도서 목록(북리스트)을 나타내는 JPA 엔티티 클래스입니다.
 * 'booklists' 테이블과 매핑됩니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "booklists")
public class Booklist {

    /**
     * 북리스트의 고유 식별자입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이 북리스트를 소유한 사용자입니다.
     * User 엔티티와 다대일(N:1) 관계입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 북리스트의 제목입니다.
     */
    @Column(length = 100, nullable = false)
    private String title;

    /**
     * 북리스트에 대한 설명입니다.
     */
    @Column(length = 1000)
    private String description;

    /**
     * 이 북리스트에 포함된 책들의 목록입니다.
     * BooklistItem 엔티티와 일대다(1:N) 관계입니다.
     * cascade = CascadeType.ALL: 북리스트가 저장/삭제될 때, 관련된 아이템들도 함께 저장/삭제됩니다.
     * orphanRemoval = true: 리스트에서 아이템이 제거되면, DB에서도 해당 아이템이 삭제됩니다.
     */
    @OneToMany(mappedBy = "booklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BooklistItem> items = new ArrayList<>();

    /**
     * 레코드 생성 시간입니다.
     */
    @CreationTimestamp
    private Timestamp createdAt;

    /**
     * 레코드 마지막 수정 시간입니다.
     */
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public Booklist(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }
}
