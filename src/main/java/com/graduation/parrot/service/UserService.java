package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.domain.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserService {
    User save(UserSaveDto userSaveDto);
    void updatePassword(String login_id, String password);
    void updateName(String login_id, String username);
    void updateAll(String login_id, UserUpdateDto userUpdateDto);
    Page<BoardListResponseDto> getUserBoardList(String login_id, Pageable pageable);
    boolean validateDuplicateUser(String login_id);
    Map<String, String> validateHandling(Errors errors);
}
