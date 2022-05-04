package com.graduation.parrot.controller;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.service.BoardService;
import com.graduation.parrot.service.CommentService;
import com.graduation.parrot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Profile("base")
@Controller
public class HomeController {

    @Autowired private UserService userService;
    @Autowired private BoardService boardService;
    @Autowired private BoardRepository boardRepository;
    @Autowired private CommentService commentService;

    @PostConstruct
    public void createUserAndBoard(){
        /*IntStream.rangeClosed(1, 100).forEach(i -> {

            UserSaveDto userSaveDto = UserSaveDto.builder()
                    .login_id("login" + i)
                    .password("pwd" + i)
                    .username("name" + i)
                    .email("email" + i + "@naver.com")
                    .build();
            User user = userService.create(userSaveDto);

            BoardDto boardDto = BoardDto.builder().title("title" + i).content("content" + i).build();
            Long boardId = boardService.create(boardDto, user);
            Board board = boardRepository.findById(boardId).get();

            commentService.create(user, board.getId(), "comment" + i);
        });*/
    }
}
