package com.graduation.parrot.domain.dto.User;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@ToString
public class UserUpdateDto {

    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;

    @Builder
    public UserUpdateDto(String username, String email, String oldPassword, String newPassword) {
        this.username = username;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
