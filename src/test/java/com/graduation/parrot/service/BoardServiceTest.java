package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BoardService boardService;

    @BeforeEach
    public void before(){
        userRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    public void boardUpdateTest(){
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

        Long update_id = boardService.update(board_id, new BoardDto("title2", "content2","2"));
        Board board = boardRepository.findById(update_id).get();

        assertThat(board.getTitle()).isEqualTo("title2");
        assertThat(board.getContent()).isEqualTo("content2");
    }

    @Test
    public void boardDeleteTest(){
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

        boardService.delete(board_id);
        //assertThat(boardRepository.count()).isEqualTo(0L);
    }

    @Test
    public void boardListTest(){
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

        boardService.create(build, savedUser);

        BoardDto build2 = BoardDto.builder()
                .title("title2")
                .content("content2")
                .build();
        boardService.create(build2, savedUser);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "board_id");
        Page<BoardListResponseDto> boardList = boardService.getBoardList(pageable, "all");

        assertThat(boardList.getTotalElements()).isEqualTo(2);
    }
}