package com.graduation.parrot.domain.dto.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class UserActivityListDto{
    private String type; //글 작성, 댓글, 추천
    private String title;
    private String author;
    private LocalDateTime createdDate;

    public UserActivityListDto(String type, UserActivityDto userActivityDto) {
        this.type = type;
        this.title = userActivityDto.getTitle();
        this.author = userActivityDto.getAuthor();
        this.createdDate = userActivityDto.getCreatedDate();
    }

}
