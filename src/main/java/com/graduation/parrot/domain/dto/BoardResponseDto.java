package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.Board;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private int view;
    private int commentCount;
    private int recommendCount;
    private String notice;
    private String createdDate;
    private String modifiedDate;
    private Long author_id;

    public BoardResponseDto(Board board, Long author_id) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor();
        this.view = board.getView();
        this.commentCount = board.getCommentCount();
        this.recommendCount = board.getRecommendCount();
        this.createdDate = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = board.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.notice = board.getNotice();
        this.author_id = author_id;
    }
}
