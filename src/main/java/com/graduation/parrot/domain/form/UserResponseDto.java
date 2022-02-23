package com.graduation.parrot.domain.form;

import com.graduation.parrot.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String login_id;
    private String username;
    private String email;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.login_id = user.getLogin_id();
        this.username = user.getName();
        this.email = user.getEmail();
    }
}
