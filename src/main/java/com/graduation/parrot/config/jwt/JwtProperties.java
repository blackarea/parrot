package com.graduation.parrot.config.jwt;

public interface JwtProperties {
	String SECRET = "cos"; // 우리 서버만 알고 있는 비밀값
	int EXPIRATION_TIME = 60000 * 10; //1분(미리세컨드) * 30
	String TOKEN_PREFIX = "Bearer ";
	String AUTHORIZATION = "Authorization";
}
