package com.taekcheonkim.todolist.user.exception;

import com.taekcheonkim.todolist.user.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler({InvalidSignInFormException.class})
    public ResponseEntity<Object> handle(InvalidSignInFormException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }
}
