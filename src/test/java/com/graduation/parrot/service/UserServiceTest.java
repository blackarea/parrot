package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void before(){
        userRepository.deleteAll();
    }

    @Test
    void updateTest(){

        UserSaveDto userSaveDto = UserSaveDto.builder().login_id("login").password("pwd").username("name").build();
        userService.save(userSaveDto);

        userService.updateName("login", "변경된이름");

        User user = userRepository.findByLogin_id("login").get();
        assertThat(user.getName()).isEqualTo("변경된이름");

    }
}