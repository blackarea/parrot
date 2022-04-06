package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Recommend;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.RecommendRepository;
import com.graduation.parrot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RecommendServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    RecommendRepository recommendRepository;
    @Autowired
    RecommendService recommendService;

    @BeforeEach
    public void before(){
        userRepository.deleteAll();
        boardRepository.deleteAll();
        recommendRepository.deleteAll();
    }

    @Test
    @DisplayName("추천")
    void recommendTest(){
        User user = User.builder()
                .login_id("login")
                .password("pwd")
                .name("name")
                .build();
        User savedUser = userRepository.save(user);

        BoardDto build = BoardDto.builder()
                .title("title")
                .content("content")
                .build();
        Long board_id = boardService.create(build, savedUser);

        Board board = boardRepository.findById(board_id).get();
        //추천

        boolean like = recommendService.like(user, board_id);
        assertThat(like).isTrue();
        Recommend foundRecommend = recommendRepository.findByUserAndBoard(user, board).get();
        assertThat(foundRecommend.getPoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("추천 취소")
    void recommendCancelTest(){
        User user = User.builder()
                .login_id("login")
                .password("pwd")
                .name("name")
                .build();
        User savedUser = userRepository.save(user);

        BoardDto build = BoardDto.builder()
                .title("title")
                .content("content")
                .build();
        Long board_id = boardService.create(build, savedUser);
        Board board = boardRepository.findById(board_id).get();

        //좋아요
        recommendService.like(user, board_id);
        //좋아요 취소
        boolean like = recommendService.like(user, board_id);

        assertThat(like).isFalse();
        assertThat(board.getRecommendCount()).isEqualTo(0);
    }

    @Transactional
    @Test
    @DisplayName("비추천에서 추천으로")
    void hateToLike(){
        User user = User.builder()
                .login_id("login")
                .password("pwd")
                .name("name")
                .build();
        User savedUser = userRepository.save(user);

        BoardDto build = BoardDto.builder()
                .title("title")
                .content("content")
                .build();
        Long board_id = boardService.create(build, savedUser);
        Board board = boardRepository.findById(board_id).get();

        recommendService.hate(user, board_id);
        assertThat(board.getRecommendCount()).isEqualTo(-1);

        boolean like = recommendService.like(user, board_id);
        assertThat(like).isTrue();
        assertThat(board.getRecommendCount()).isEqualTo(1);
    }
}