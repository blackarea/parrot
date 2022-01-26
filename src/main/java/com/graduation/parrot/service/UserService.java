package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.UserSaveForm;

public interface UserService {
    User save(UserSaveForm userSaveForm);
}
