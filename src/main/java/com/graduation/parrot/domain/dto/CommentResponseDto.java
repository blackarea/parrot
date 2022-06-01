package com.graduation.parrot.domain.dto;

import com.graduation.parrot.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    private Long id;
    private String author;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private int recommendCount;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = comment.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.recommendCount = comment.getRecommendCount();
    }
}
