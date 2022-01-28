package com.graduation.parrot.domain.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveForm {

    private String login_id;
    private String password;
    private String username;
    private String email;

    @Builder
    public UserSaveForm(String login_id, String password, String username, String email) {
        this.login_id = login_id;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    @Builder
    public UserSaveForm(String login_id, String password, String username) {
        this.login_id = login_id;
        this.password = password;
        this.username = username;
    }
}
