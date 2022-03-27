package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.domain.dto.UserUpdateDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void before(){
        userRepository.deleteAll();/*
        UserSaveDto userSaveDto = UserSaveDto.builder().login_id("login").password("pwd").username("name").build();
        userService.save(userSaveDto);*/
    }

    @Test
    void updateNameTest(){
        userService.updateName("login", "변경된이름");

        User user = userRepository.findByLogin_id("login").get();
        assertThat(user.getName()).isEqualTo("변경된이름");
    }

    @Test
    void updateAllTest(){
        UserUpdateDto userUpdateDto = new UserUpdateDto("changename", "changeemail");
        userService.updateAll("login", userUpdateDto);

        User user = userRepository.findByLogin_id("login").get();

        assertThat(user.getName()).isEqualTo("changename");
        assertThat(user.getEmail()).isEqualTo("changeemail");
    }

    @Test
    void userBoardListTest(){
        User user = userRepository.save(User.builder().login_id("test1").password("test2").name("test3").build());
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Board board = new Board("title"+i, "content" + i, user);
            boardRepository.save(board);
        });
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "board_id");
        Page<BoardListResponseDto> userBoardList = userService.getUserBoardList(user.getLogin_id(), pageable);
        assertThat(userBoardList.getTotalPages()).isEqualTo(3);
        assertThat(userBoardList.getTotalElements()).isEqualTo(5);
        BoardListResponseDto boardListResponseDto = userBoardList.getContent().get(0);
        assertThat(boardListResponseDto.getTitle()).isEqualTo("title5");
    }
}