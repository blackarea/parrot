package com.graduation.parrot.domain.form;

import com.graduation.parrot.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardForm {

    private String title;
    private String content;

    @Builder
    public BoardForm(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
