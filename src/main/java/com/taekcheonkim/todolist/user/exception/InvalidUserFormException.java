package com.taekcheonkim.todolist.user.exception;

public class InvalidUserFormException extends IllegalArgumentException {
    public InvalidUserFormException(String s) {
        super(s);
    }
}
