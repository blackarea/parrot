package com.graduation.parrot.domain.form;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String login_id;
	private String password;
}
