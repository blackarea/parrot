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

    public BoardResponseForm(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor();
    }
}
