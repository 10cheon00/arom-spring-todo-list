package com.taekcheonkim.todolist.repository;

import com.taekcheonkim.todolist.domain.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TodoRepository {
    void save(Todo todo);
    List<Todo> findAll();
    Todo findById(Long id);
    void update(Todo todo);
    void delete(Todo todo);
    void deleteAll();
}