package com.graduation.parrot.service;

import com.graduation.parrot.domain.Comment;
import com.graduation.parrot.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public Long save(Comment comment) {
        return commentRepository.save(comment).getId();
    }

    @Override
    public Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다."));
        return comment;
    }

    @Transactional
    @Override
    public void update(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다."));
        comment.update(content);
    }

    @Override
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다."));
        commentRepository.delete(comment);
    }
}
