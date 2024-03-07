package com.taekcheonkim.todolist.user.exception;

import com.taekcheonkim.todolist.user.dto.ErrorMessageDto;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler({InvalidSignInFormException.class})
    public ResponseEntity<ErrorMessageDto> handle(InvalidSignInFormException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidSignUpFormException.class})
    public ResponseEntity<ErrorMessageDto> handle(InvalidSignUpFormException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorMessageDto> handle(HttpMessageNotReadableException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }
}
