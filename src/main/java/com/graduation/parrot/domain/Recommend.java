package com.graduation.parrot.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Recommend extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "recommend_id")
    private Long id;

    @Column(columnDefinition = "integer default 0", updatable = false)
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

}
