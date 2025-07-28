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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "booklists")
public class Booklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;
    
    // 북리스트가 삭제되면, 연관된 아이템들도 함께 삭제됩니다.
    @OneToMany(mappedBy = "booklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BooklistItem> items = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public Booklist(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }
}