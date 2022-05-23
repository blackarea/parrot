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

    @Column(nullable = false)
    private String title;

    @Column(length = 10000)
    private String content;
    private String author;

    @Column(columnDefinition = "integer default 0")
    private int view;

    @Column(columnDefinition = "integer default 0")
    private int commentCount;

    @Column(name = "recommend_count", columnDefinition = "integer default 0")
    private int recommendCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private List<File> files = new ArrayList<>();

    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.author = user.getName();
        setUser(user);
    }

    public Board(String title, String content, User user, File file) {
        this.title = title;
        this.content = content;
        this.author = user.getName();
        setUser(user);
        setFile(file);
    }

    private void setFile(File file) {
        this.files.add(file);
    }

    public void setUser(User user) {
        this.user = user;
        user.getBoards().add(this);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateCommentCount(int point){
        this.commentCount += point;
    }

    public void updateRecommendCount(int point){
        this.recommendCount = recommendCount + point;
    }
}
