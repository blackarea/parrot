package com.graduation.parrot.domain.dto.User;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class UserActivityPageDto {

    private int boardCount;
    private int commentCount;
    private Page<UserActivityListDto> page;

    public UserActivityPageDto(int boardCount, int commentCount, Page<UserActivityListDto> page) {
        this.boardCount = boardCount;
        this.commentCount = commentCount;
        this.page = page;
    }
}
