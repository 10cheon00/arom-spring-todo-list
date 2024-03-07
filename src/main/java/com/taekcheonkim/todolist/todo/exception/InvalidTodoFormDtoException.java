package com.taekcheonkim.todolist.todo.exception;

public class InvalidTodoFormDtoException extends IllegalArgumentException{
    public InvalidTodoFormDtoException(String s) {
        super(s);
    }
}
