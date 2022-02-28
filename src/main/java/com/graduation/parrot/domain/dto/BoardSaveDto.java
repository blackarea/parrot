package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSaveDto {

    private String title;
    private String content;
    User user;

    @Builder
    public BoardSaveDto(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Board toEntity(){
        return new Board(title, content, user);
    }
}
