package com.graduation.parrot.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@ToString
public class UserUpdateDto {

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String username;
    private String email;

    @Builder
    public UserUpdateDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
