package com.graduation.parrot.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionEntity {
    private String errorCode;
    private String errorMessage;

    @Builder
    public ExceptionEntity(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
