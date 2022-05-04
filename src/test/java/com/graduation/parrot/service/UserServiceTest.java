package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Comment;
import com.graduation.parrot.domain.Recommend;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.ParrotStateDto;
import com.graduation.parrot.domain.dto.User.UserActivityListDto;
import com.graduation.parrot.domain.dto.User.UserActivityPageDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.CommentRepository;
import com.graduation.parrot.repository.RecommendRepository;
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
import org.springframework.transaction.annotation.Transactional;

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
    CommentRepository commentRepository;
    @Autowired
    RecommendRepository recommendRepository;
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
    void getUserActivityPagingTest(){
        User user = userRepository.save(User.builder().login_id("login").password("pwd").name("name").build());
        User user2 = userRepository.save(User.builder().login_id("login2").password("pwd2").name("name2").build());
        IntStream.rangeClosed(1, 2).forEach(i -> {
            Board board = new Board("title"+i, "content" + i, user2);
            boardRepository.save(board);
            Comment comment = new Comment("comment content" + i, user, board);
            commentRepository.save(comment);
            Recommend recommend = new Recommend(user, board, 1);
            recommendRepository.save(recommend);
        });

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "board_id");
        UserActivityPageDto userActivityPageDto = userService.getUserActivityPaging("login", pageable);
        assertThat(userActivityPageDto.getPage().getNumberOfElements()).isEqualTo(4);

    }

    @Test
    void parrotStateTest(){
        UserSaveDto userSaveDto = new UserSaveDto("login", "pwd", "name", "email");
        userService.create(userSaveDto);

        ParrotStateDto makeParrotStateDto = ParrotStateDto.builder()
                .hunger("hunger").stress("stresss").boredom("boredom")
                .affection("affection").level("level")
                .counter("counter").lastTime(123).build();
        userService.setParrotState("login", makeParrotStateDto);
        ParrotStateDto parrotStateDto = userService.getParrotState("login");
        assertThat(parrotStateDto.getStress()).isEqualTo("stresss");
    }
}