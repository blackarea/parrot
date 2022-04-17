package com.graduation.parrot.domain.dto.User;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserSaveDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String login_id;

    //TODO password @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}"
    // , message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String username;
    private String email = null;

    @Builder
    public UserSaveDto(String login_id, String password, String username, String email) {
        this.login_id = login_id;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    @Builder
    public UserSaveDto(String login_id, String password, String username) {
        this.login_id = login_id;
        this.password = password;
        this.username = username;
    }
}
