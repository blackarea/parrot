package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long author_id;

    public BoardResponseDto(Board board, Long author_id) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor();
        this.author_id = author_id;
    }
}
