package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserService {
    User create(UserSaveDto userSaveDto);
    void updatePassword(String login_id, String password);
    void updateName(String login_id, String username);
    void updateEmail(String login_id, String email);
    Page<UserActivityListDto> getUserActivityPaging(String login_id, Pageable pageable);
    boolean validateDuplicateUser(String login_id);
    Map<String, String> validateHandling(Errors errors);
}
