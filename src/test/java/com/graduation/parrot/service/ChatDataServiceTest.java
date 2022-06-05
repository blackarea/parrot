package com.graduation.parrot.service;

import com.graduation.parrot.domain.ChatData;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.ChatDataDto;
import com.graduation.parrot.repository.ChatDataRepository;
import com.graduation.parrot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChatDataServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatDataService chatDataService;
    @Autowired
    ChatDataRepository chatDataRepository;

    @BeforeEach
    public void before(){
        userRepository.deleteAll();
        chatDataRepository.deleteAll();
        User user = User.builder()
                .login_id("login")
                .password("pwd")
                .name("name")
                .build();
        userRepository.save(user);
    }

    @Test
    void testAll() {
        ChatDataDto chatDataDto = new ChatDataDto(0, "1", 0, "1", 0, 0, 0, 0);
        Long id = chatDataService.create("login", chatDataDto);
        chatDataDto = new ChatDataDto(1, "1", 0, "1", 0, 0, 0, 0);
        Long id2 = chatDataService.create("login", chatDataDto);

        List<ChatData> allChatData = chatDataService.getAllChatData("login");
        assertThat(allChatData.size()).isEqualTo(2);

        chatDataService.updateChatData("login", 2);
        ChatData updatedChatData = chatDataRepository.findByUserIdAndId("login", id);
        ChatData updatedChatData2 = chatDataRepository.findByUserIdAndId("login", id2);
        assertThat(updatedChatData.getRadio()).isEqualTo(2);
        assertThat(updatedChatData2.getRadio()).isEqualTo(2);

        chatDataService.deleteChatData("login", id);
        List<ChatData> deleteResult = chatDataService.getAllChatData("login");
        assertThat(deleteResult.size()).isEqualTo(1);
    }

}