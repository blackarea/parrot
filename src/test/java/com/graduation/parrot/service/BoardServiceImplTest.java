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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardServiceImplTest {

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

        Long board_id = boardService.insert(build, savedUser);

        Long update_id = boardService.update(board_id, new BoardDto("title2", "content2"));
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

        Long board_id = boardService.insert(build, savedUser);

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

        Long board_id = boardService.insert(build, savedUser);

        BoardDto build2 = BoardDto.builder()
                .title("title2")
                .content("content2")
                .build();
        boardService.insert(build2, savedUser);

        List<BoardListResponseDto> boardList = boardService.getBoardList();
        assertThat(boardList.size()).isEqualTo(2);

        for (BoardListResponseDto boardListResponseDto : boardList) {
            System.out.println("boardListResponseForm = " + boardListResponseDto);
        }
    }
}