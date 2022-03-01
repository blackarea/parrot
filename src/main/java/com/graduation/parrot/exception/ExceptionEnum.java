package com.graduation.parrot.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    ALREADY_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 이이디입니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),

    /** @VALID **/
    NOT_BLANK(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다.");

    private HttpStatus httpStatus;
    private String errorMessage;

    ExceptionEnum(HttpStatus httpStatus, String errormessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errormessage;
    }
}
