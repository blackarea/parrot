package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardListResponseDto {

    private Long id;
    private String title;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BoardListResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.name = board.getAuthor();
        this.createdDate = board.getCreatedDate();
        this.modifiedDate = board.getModifiedDate();
    }
}
