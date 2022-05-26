package com.graduation.parrot.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "content"})
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;
    private String author;

    @Column(columnDefinition = "integer default 0")
    private int recommendCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentRecommend> commentRecommends = new ArrayList<>();

    @Builder
    public Comment(String content, User user, Board board) {
        this.content = content;
        this.author = user.getName();
        setUser(user);
        setBoard(board);
        this.board = board;
    }

    public void setUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }

    private void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    public void update(String content){
        this.content = content;
    }

    public void updateRecommend(int point){
        this.recommendCount += point;
    }
}
