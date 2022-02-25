package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.UserSaveDto;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserService {
    User save(UserSaveDto userSaveDto);
    boolean validateDuplicateUser(String login_id);
    Map<String, String> validateHandling(Errors errors);
}
