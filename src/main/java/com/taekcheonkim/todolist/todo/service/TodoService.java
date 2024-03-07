package com.taekcheonkim.todolist.todo.service;

import com.taekcheonkim.todolist.todo.domain.Todo;
import com.taekcheonkim.todolist.todo.dto.TodoFormDto;
import com.taekcheonkim.todolist.todo.repository.TodoRepository;
import com.taekcheonkim.todolist.user.authorization.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@PreAuthorize
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo save(TodoFormDto todoFormDto) {
        return null;
    }

    public List<Todo> findAll() {
        return null;
    }

    public Todo findById(Long todoId) {
        return null;
    }

    public Todo update(TodoFormDto updateTodoForm) {
        return null;
    }

    public void deleteById(Long savedTodoId) {
    }
}
