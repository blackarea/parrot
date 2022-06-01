package com.graduation.parrot.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "comment_recommend")
public class CommentRecommend {

    @Id @GeneratedValue
    @Column(name = "comment_recommend_id")
    private Long id;

    @Column(columnDefinition = "integer default 0", updatable = false)
    private int point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", updatable = false)
    private Comment comment;

    @Builder
    public CommentRecommend(User user, Comment comment, int point){
        this.user = user;
        this.comment = comment;
        this.point = point;
    }
}
