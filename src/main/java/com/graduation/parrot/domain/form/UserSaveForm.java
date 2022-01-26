package com.graduation.parrot.domain.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSaveForm {

    private String login_id;
    private String password;
    private String username;
    private String email;
}
