package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public User save(UserSaveDto userSaveDto) {
        User user;
        if (userSaveDto.getEmail() == null) {
            user = User.builder()
                    .login_id(userSaveDto.getLogin_id())
                    .password(bCryptPasswordEncoder.encode(userSaveDto.getPassword()))
                    .name(userSaveDto.getUsername())
                    .build();
        } else {
            user = User.builder()
                    .login_id(userSaveDto.getLogin_id())
                    .password(bCryptPasswordEncoder.encode(userSaveDto.getPassword()))
                    .name(userSaveDto.getUsername())
                    .email(userSaveDto.getEmail())
                    .build();
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void updateName(String login_id, String username) {
        User user = userRepository.findByLogin_id(login_id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다. login_id" + login_id));
        user.update(username);
    }

    @Override
    public boolean validateDuplicateUser(String login_id) {
        Optional<User> user = userRepository.findByLogin_id(login_id);
        return user.isPresent();
    }

    @Override
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

}
