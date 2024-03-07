package com.taekcheonkim.todolist.user.exception;

public class InvalidSignInFormException extends IllegalArgumentException {
    public InvalidSignInFormException(String s) {
        super(s);
    }
}
