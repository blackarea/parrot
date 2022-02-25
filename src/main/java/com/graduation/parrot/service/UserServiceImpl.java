package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public User save(UserSaveDto userSaveDto) {
        if(userSaveDto.getEmail().equals("")) {
            User user = User.builder()
                    .login_id(userSaveDto.getLogin_id())
                    .password(bCryptPasswordEncoder.encode(userSaveDto.getPassword()))
                    .name(userSaveDto.getUsername())
                    .build();
            userRepository.save(user);
            return user;
        }
        else {
            User user = User.builder()
                    .login_id(userSaveDto.getLogin_id())
                    .password(bCryptPasswordEncoder.encode(userSaveDto.getPassword()))
                    .name(userSaveDto.getUsername())
                    .email(userSaveDto.getEmail())
                    .build();
            userRepository.save(user);
            return user;
        }
    }

    @Override
    public boolean validateDuplicateUser(String login_id) {
        Optional<User> user = userRepository.findByLogin_id(login_id);
        if(user.isEmpty()){
            return false;
        }
        return true;
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
