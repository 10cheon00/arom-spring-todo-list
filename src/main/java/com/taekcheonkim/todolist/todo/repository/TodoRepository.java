package com.taekcheonkim.todolist.todo.repository;

import com.taekcheonkim.todolist.todo.domain.Todo;

import java.util.List;

public interface TodoRepository {
    Long save(Todo todo);
    List<Todo> findAll();
    Todo findById(Long id);
    boolean isExistById(Long id);
    void update(Todo todo);
    void delete(Todo todo);
    void deleteAll();
}