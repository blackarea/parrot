package com.graduation.parrot.domain.form;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSaveForm {

    private String title;
    private String content;
    User user;

    @Builder
    public BoardSaveForm(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Board toEntity(){
        return new Board(title, content, user);
    }
}
