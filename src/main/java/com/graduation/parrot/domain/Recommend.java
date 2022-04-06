package com.graduation.parrot.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Recommend {

    @Id @GeneratedValue
    @Column(name = "recommend_id")
    private Long id;

    private int point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", updatable = false)
    private Board board;

    @Builder
    public Recommend(User user, Board board, int point){
        this.user = user;
        this.board = board;
        this.point = point;
    }

    public void updatePoint(int point){
        this.point = point;
    }
}
