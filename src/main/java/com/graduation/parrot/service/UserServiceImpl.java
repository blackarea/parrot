package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.UserSaveForm;
import com.graduation.parrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User save(UserSaveForm userSaveForm) {
        if(userSaveForm.getEmail() != "") {
            User user = User.builder()
                    .login_id(userSaveForm.getLogin_id())
                    .password(userSaveForm.getPassword())
                    .name(userSaveForm.getUsername())
                    .email(userSaveForm.getEmail())
                    .build();
            userRepository.save(user);
            return user;
        }
        else {
            User user = User.builder()
                    .login_id(userSaveForm.getLogin_id())
                    .password(userSaveForm.getPassword())
                    .name(userSaveForm.getUsername())
                    .build();
            userRepository.save(user);
            return user;
    }
}
