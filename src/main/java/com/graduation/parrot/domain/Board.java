package com.graduation.parrot.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "title", "content"})
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(length = 1000)
    private String content;

    @Column(updatable = false)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.author = user.getName();
        setUser(user);
    }

    public void setUser(User user) {
        this.user = user;
        user.getBoards().add(this);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
