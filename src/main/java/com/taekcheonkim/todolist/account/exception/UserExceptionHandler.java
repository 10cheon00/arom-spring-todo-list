package com.taekcheonkim.todolist.account.exception;

import com.taekcheonkim.todolist.account.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler({InvalidUserFormException.class})
    public ResponseEntity<Object> handle(InvalidUserFormException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }
}
