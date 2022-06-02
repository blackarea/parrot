package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Comment;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.CommentResponseDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.CommentRepository;
import com.graduation.parrot.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.graduation.parrot.domain.QBoard.board;
import static org.assertj.core.api.Assertions.assertThat;

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
    @PersistenceContext
    EntityManager em;

    private Long commentId;
    private Long boardId;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
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

        boardId = foundBoard.getId();
        commentId = commentService.create(user, board.getId(), "댓글");
    }

    @Test
    void createComment() {
        User user = userRepository.findByLogin_id("login").get();
        Board foundBoard = queryFactory
                .selectFrom(board)
                .where(board.title.eq("제목"))
                .fetchOne();
        commentService.create(user, foundBoard.getId(), "댓글내용");
    }

    @Test
    void updateAndDeleteTest() {
        commentService.update(commentId, "업뎃내용");
        Comment comment = commentService.getComment(commentId);
        assertThat(comment.getContent()).isEqualTo("업뎃내용");

        commentService.delete(boardId ,commentId);
        assertThat(commentRepository.count()).isEqualTo(0L);
    }

    @Test
    void getCommentListTest() {
        //query 얼마나 나가는지 확인
        Board foundBoard = queryFactory
                .selectFrom(board)
                .where(board.title.eq("제목"))
                .fetchOne();

        List<CommentResponseDto> commentList = commentService.getCommentList(foundBoard.getId());
        assertThat(commentList.size()).isEqualTo(1);
    }

    @Test
    void getBestCommentList() {
        User user = userRepository.findByLogin_id("login").get();
        Board foundBoard = queryFactory
                .selectFrom(board)
                .where(board.title.eq("제목"))
                .fetchOne();
        Long[] commentId = new Long[3];
        for (int i = 0; i < 3; i++) {
            commentId[i] = commentService.create(user, foundBoard.getId(), "test" + i);
        }
        for (int i = 0; i < 5; i++) {
            User savedUser = userRepository.save(new User("login" + i, "pwd" + i, "name" + i, "test"));
            commentService.recommend(savedUser, commentId[0], 1);
            commentService.recommend(savedUser, commentId[1], 1);
            commentService.recommend(savedUser, commentId[2], 1);
        }
        List<CommentResponseDto> bestCommentList = commentService.getBestCommentList(foundBoard.getId());
        assertThat(bestCommentList.size()).isEqualTo(3);
        assertThat(bestCommentList.get(0).getRecommendCount()).isEqualTo(5);
    }

}