package com.graduation.parrot.repository;

import com.graduation.parrot.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @BeforeEach
    void before(){
        userRepository.deleteAll();
    }

    @Test
    void findByEmail() {
        User user = userRepository.save(User.builder().login_id("login")
                .password("pwd").name("name").email("test@naver.com").build());

        Optional<User> foundUser = userRepository.findByEmail("test@naver.com");
        assertThat(foundUser).isPresent();
    }
}