package com.graduation.parrot.service;

import com.graduation.parrot.domain.Comment;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.CommentResponseDto;
import com.graduation.parrot.domain.dto.RecommendDto;

import java.util.List;
import java.util.Map;

public interface CommentService {
    Long create(User user, Long board_id, String content);
    Comment getComment(Long comment_id);
    void update(Long comment_id, String content);
    void delete(Long board_id, Long comment_id);
    List<CommentResponseDto> getCommentList(Long board_id);
    RecommendDto recommend(User user, Long comment_id, int point);
    Map<String, String> recommendCheck(User user, Long comment_id);
}
