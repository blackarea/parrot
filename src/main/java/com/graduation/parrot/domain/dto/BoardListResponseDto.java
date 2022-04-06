package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class BoardListResponseDto {

    private Long id;
    private String title;
    private String author;
    private int view;
    private int recommendCount;
    private String createdDate;
    private String modifiedDate;

    public BoardListResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.author = board.getAuthor();
        this.view = board.getView();
        this.recommendCount = board.getRecommendCount();
        this.createdDate = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = board.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
