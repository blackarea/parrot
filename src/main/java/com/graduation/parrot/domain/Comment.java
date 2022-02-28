package com.graduation.parrot.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(String content, User user, Board board) {
        this.content = content;
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
}
