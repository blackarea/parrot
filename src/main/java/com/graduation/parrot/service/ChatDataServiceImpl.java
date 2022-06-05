package com.graduation.parrot.service;

import com.graduation.parrot.domain.ChatData;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.ChatDataDto;
import com.graduation.parrot.repository.ChatDataRepository;
import com.graduation.parrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatDataServiceImpl implements ChatDataService{

    private final ChatDataRepository chatDataRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Long create(String login_id, ChatDataDto chatDataDto) {
        User user = userRepository.findByLogin_id(login_id).get();
        return chatDataRepository.save(new ChatData(user, chatDataDto)).getId();
    }

    @Override
    public List<ChatData> getAllChatData(String login_id) {
        return chatDataRepository.findAllByUserId(login_id);
    }

    @Transactional
    @Override
    public void updateChatData(String login_id, int radio) {
        User user = userRepository.findByLogin_id(login_id).get();
        chatDataRepository.updateAllRadio(radio, user);
    }

    @Override
    public void deleteChatData(String login_id, Long chatData_id) {
        ChatData chatData = chatDataRepository.findByUserIdAndId(login_id, chatData_id);
        chatDataRepository.delete(chatData);
    }
}
