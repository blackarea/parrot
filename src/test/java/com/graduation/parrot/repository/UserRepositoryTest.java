package com.graduation.parrot.repository;

import com.graduation.parrot.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    public void findById쿼리테스트(){
        Optional<User> byId = userRepository.findByLogin_id("login1");
        System.out.println(byId.get());
    }

}