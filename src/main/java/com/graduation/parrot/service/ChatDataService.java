package com.graduation.parrot.service;

import com.graduation.parrot.domain.ChatData;
import com.graduation.parrot.domain.dto.ChatDataDto;

import java.util.List;

public interface ChatDataService {
    Long create(String login_id, ChatDataDto chatDataDto);
    List<ChatData> getAllChatData(String login_id);
    void updateChatData(String login_id, int radio);
    void deleteChatData(String login_id, Long chatData_id);
}
