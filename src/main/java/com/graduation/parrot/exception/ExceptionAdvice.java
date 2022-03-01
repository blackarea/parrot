package com.graduation.parrot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionEntity> handleBaseEx(ApiException e){
        String errorCode = String.valueOf(e.getExceptionEnum().getHttpStatus().value());
        String errorMessage = e.getMessage();
        return new ResponseEntity<>(new ExceptionEntity(errorCode, errorMessage), e.getExceptionEnum().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionEntity> handleBaseEx(MethodArgumentNotValidException e){
        String errorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(new ExceptionEntity(errorCode, errorMessage), HttpStatus.BAD_REQUEST);
    }


}
