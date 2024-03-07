package com.taekcheonkim.todolist;

import com.taekcheonkim.todolist.user.dto.ErrorMessageDto;
import com.taekcheonkim.todolist.user.exception.AccessDeniedException;
import com.taekcheonkim.todolist.user.exception.InvalidSignInFormException;
import com.taekcheonkim.todolist.user.exception.InvalidSignUpFormException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidSignInFormException.class})
    public ResponseEntity<ErrorMessageDto> handle(InvalidSignInFormException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidSignUpFormException.class})
    public ResponseEntity<ErrorMessageDto> handle(InvalidSignUpFormException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handle(AccessDeniedException e) {
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorMessageDto> handle(HttpMessageNotReadableException e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return new ResponseEntity<>(errorMessageDto, HttpStatus.BAD_REQUEST);
    }
}
