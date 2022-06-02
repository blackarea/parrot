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
    private String notice;

    @Builder
    public BoardDto(String title, String content, String notice) {
        this.title = title;
        this.content = content;
        this.notice = notice;
    }

}
