package com.graduation.parrot.validator;

import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class SignUpValidator extends AbstractValidator<UserSaveDto> {

    private final UserService userService;

    @Override
    protected void doValidate(UserSaveDto dto, Errors errors) {
        if(userService.validateDuplicateUser(dto.getLogin_id())){
            errors.rejectValue("login_id", "아이디 중복 오류", "이미 사용중인 아이디입니다.");
        }
    }
}
