package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

import static com.graduation.parrot.domain.QBoard.board;
import static org.assertj.core.api.Assertions.assertThat;

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
    @PersistenceContext
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void before(){
        queryFactory = new JPAQueryFactory(em);
        userRepository.deleteAll();
    }

    @Test
    void updateNameTest(){
        User user = userRepository.save(User.builder().login_id("login").password("pwd").name("name").build());
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Board board = new Board("title"+i, "content" + i, user);
            boardRepository.save(board);
        });
        userService.updateName("login", "변경된이름");

        User foundUser = userRepository.findByLogin_id("login").get();
        assertThat(foundUser.getName()).isEqualTo("변경된이름");

        List<Board> boardList = queryFactory
                .selectFrom(board)
                .where(board.author.eq("변경된이름"))
                .fetch();
        assertThat(boardList.size()).isEqualTo(5);
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