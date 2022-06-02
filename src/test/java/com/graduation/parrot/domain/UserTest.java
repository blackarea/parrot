package com.graduation.parrot.domain;

import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    public void before(){
        userRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    public void cascadeTest(){
        User user = User.builder()
                .login_id("login")
                .password("pwd")
                .name("name")
                .build();
        User savedUser = userRepository.save(user);

        Board board = new Board("title", "content", savedUser, "1");
        Board savedBoard = boardRepository.save(board);

        assertThat(boardRepository.count()).isEqualTo(1L);
        userRepository.deleteAll();
        assertThat(boardRepository.count()).isEqualTo(0L);
    }
}