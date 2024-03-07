package com.taekcheonkim.todolist.todo.controller;

import com.taekcheonkim.todolist.todo.domain.Todo;
import com.taekcheonkim.todolist.todo.dto.TodoFormDto;
import com.taekcheonkim.todolist.todo.service.TodoService;
import com.taekcheonkim.todolist.user.authorization.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize
@RequestMapping(TodoController.UriPath)
public class TodoController {
    public final static String UriPath = "/todos";

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public List<Todo> readAll() {
        return todoService.findByCurrentUser();
    }

    @PostMapping("")
    public Todo createTodo(@RequestBody TodoFormDto todoFormDto) {
        return todoService.createTodo(Optional.ofNullable(todoFormDto));
    }

    @PutMapping("")
    public Todo updateTodo(@RequestBody TodoFormDto todoFormDto) {
        return todoService.updateTodo(Optional.ofNullable(todoFormDto));
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@RequestBody Long id) {
        todoService.deleteById(Optional.ofNullable(id));
    }
}
