package com.graduation.parrot.controller;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Profile("base")
@Controller
public class HomeController {

    @Autowired private UserRepository userRepository;
    @Autowired private BoardRepository boardRepository;

    @PostConstruct
    public void createUserAndBoard(){
        IntStream.rangeClosed(1, 5).forEach(i -> {
            User user = User.builder()
                    .login_id("login" + i)
                    .password("pwd" + i)
                    .name("name" + i)
                    .build();
            userRepository.save(user);

            Board board = new Board("title"+i, "content"+i, user);
            boardRepository.save(board);
        });
    }
}
