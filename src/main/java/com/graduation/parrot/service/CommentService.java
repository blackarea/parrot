package com.graduation.parrot.service;

import com.graduation.parrot.domain.Comment;

public interface CommentService {
    Long save(Comment comment);
    Comment getComment(Long commentId);
    void update(Long commentId, String content);
    void delete(Long commentId);
}
