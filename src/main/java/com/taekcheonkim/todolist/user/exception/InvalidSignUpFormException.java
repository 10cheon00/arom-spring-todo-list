package com.taekcheonkim.todolist.user.exception;

public class InvalidSignUpFormException extends IllegalArgumentException {
    public InvalidSignUpFormException(String s) {
        super(s);
    }
}
