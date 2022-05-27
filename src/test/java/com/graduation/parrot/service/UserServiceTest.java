package com.graduation.parrot.service;

import com.graduation.parrot.domain.*;
import com.graduation.parrot.domain.dto.ParrotStateDto;
import com.graduation.parrot.domain.dto.User.ParrotDataDto;
import com.graduation.parrot.domain.dto.User.UserActivityPageDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.graduation.parrot.domain.QBoard.board;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    ParrotDataRepository parrotDataRepository;
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
        User user = makeUser();

        ParrotStateDto makeParrotStateDto = ParrotStateDto.builder()
                .hunger("hunger").stress("stresss").boredom("boredom")
                .affection("affection").level("level")
                .counter("counter").lastTime(123).build();
        userService.setParrotState("login", makeParrotStateDto);
        ParrotStateDto parrotStateDto = userService.getParrotState("login");
        assertThat(parrotStateDto.getStress()).isEqualTo("stresss");
    }

    @Test
    void withdrawTest(){
        User user = makeUser();

        userService.withdraw("login");
        List<User> all = userRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void ParrotData(){
        User user = makeUser();
        makeParrotData();

        Optional<ParrotData> foundData = parrotDataRepository.findByPageAndUserLogin_id(1, user.getLogin_id());
        assertThat(foundData).isPresent();

        Optional<ParrotDataDto> foundParrotDataDto = userService.getParrotData(user.getLogin_id(), 1);
        assertThat(foundParrotDataDto.get().getPage()).isEqualTo(1);
        assertThat(foundParrotDataDto.get().getDate()).isEqualTo("2022-05-20");
        Optional<ParrotDataDto> foundParrotDataDto2 = userService.getParrotData(user.getLogin_id(), 2);
        assertThat(foundParrotDataDto2).isEmpty();
    }

    @Test
    void parrotDataSize(){
        User user = makeUser();
        assertThat(userService.getParrotDataPageSize(user.getLogin_id())).isEqualTo(0);
        makeParrotData();

        assertThat(userService.getParrotDataPageSize(user.getLogin_id())).isEqualTo(1);
    }

    private User makeUser() {
        UserSaveDto userSaveDto = new UserSaveDto("login", "pwd", "name", "email");
        return userService.create(userSaveDto);
    }

    private void makeParrotData() {
        ParrotDataDto parrotDataDto =
                new ParrotDataDto(1, "2022-05-20", 1, 1, 1,
                        1, 1, 1);
        userService.setParrotData("login", parrotDataDto);
    }
}