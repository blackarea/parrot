package com.graduation.parrot.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    private String title;
    private String content;

    @Builder
    public BoardDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
