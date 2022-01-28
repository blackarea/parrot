package com.graduation.parrot.domain.form;

import com.graduation.parrot.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseForm {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long author_id;

    public BoardResponseForm(Board board, Long author_id) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor();
        this.author_id = author_id;
    }
}
