package com.graduation.parrot.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserActivityListDto {
    private String type; //글 작성, 댓글, 추천
    private String title;
    private String author;

    public UserActivityListDto(String type, UserActivityDto userActivityDto) {
        this.type = type;
        this.title = userActivityDto.getTitle();
        this.author = userActivityDto.getAuthor();
    }
}
