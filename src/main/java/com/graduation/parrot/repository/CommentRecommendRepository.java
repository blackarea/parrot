package com.graduation.parrot.repository;

import com.graduation.parrot.domain.Comment;
import com.graduation.parrot.domain.CommentRecommend;
import com.graduation.parrot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRecommendRepository extends JpaRepository<CommentRecommend, Long> {
    Optional<CommentRecommend> findByUserAndComment(User user, Comment comment);
}
