package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Comment;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.CommentRepository;
import com.graduation.parrot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    private Long commentId;

    @BeforeEach
    public void before(){
        commentRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        User user = User.builder()
                .login_id("login")
                .password("pwd")
                .name("name")
                .build();
        User foundUser = userRepository.save(user);
        Board board = Board.builder()
                .user(foundUser)
                .title("제목")
                .content("내용")
                .build();
        Board foundBoard = boardRepository.save(board);

        Comment comment = Comment.builder()
                .user(foundUser)
                .board(foundBoard)
                .content("댓글")
                .build();
        commentId = commentService.save(comment);
    }

    @Test
    void saveComment() {
        Comment foundComment = commentService.getComment(commentId);
        assertThat(foundComment.getId()).isEqualTo(commentId);
        assertThat(commentRepository.count()).isEqualTo(1);
    }

    @Test
    void updateAndDeleteTest(){
        commentService.update(commentId, "업뎃내용");
        Comment comment = commentService.getComment(commentId);
        assertThat(comment.getContent()).isEqualTo("업뎃내용");

        commentService.delete(commentId);
        assertThat(commentRepository.count()).isEqualTo(0L);
    }

}