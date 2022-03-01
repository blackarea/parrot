package com.graduation.parrot.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private ExceptionEnum exceptionEnum;

    public ApiException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getErrorMessage());
        this.exceptionEnum = exceptionEnum;
    }
}
