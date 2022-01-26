package com.graduation.parrot.domain.form;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardListResponseForm {

    private Long id;
    private String title;
    private String name;
    private LocalDateTime modifiedDate;

    public BoardListResponseForm(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.name = board.getUser().getName();
        this.modifiedDate = board.getModifiedDate();
    }
}
