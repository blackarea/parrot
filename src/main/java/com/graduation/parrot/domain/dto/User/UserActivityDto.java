package com.graduation.parrot.domain.dto.User;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class UserActivityDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdDate;

    @QueryProjection
    public UserActivityDto(Long id, String title, String author, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdDate = createdDate;
    }
}
