package com.graduation.parrot.domain.dto.User;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserActivityDto {

    private String title;
    private String author;

    @QueryProjection
    public UserActivityDto(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
