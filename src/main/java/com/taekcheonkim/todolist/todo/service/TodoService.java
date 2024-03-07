package com.taekcheonkim.todolist.todo.service;

import com.taekcheonkim.todolist.todo.domain.Todo;
import com.taekcheonkim.todolist.todo.dto.TodoFormDto;
import com.taekcheonkim.todolist.todo.exception.InvalidTodoFormDtoException;
import com.taekcheonkim.todolist.todo.repository.TodoRepository;
import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final AuthenticationContext authenticationContext;

    @Autowired
    public TodoService(TodoRepository todoRepository, AuthenticationContext authenticationContext) {
        this.todoRepository = todoRepository;
        this.authenticationContext = authenticationContext;
    }

    public Todo createTodo(Optional<TodoFormDto> maybeTodoFormDto) {
        if (maybeTodoFormDto.isEmpty()) {
            throw new InvalidTodoFormDtoException("Request with invalid form.");
        }
        TodoFormDto todoFormDto = maybeTodoFormDto.get();

        if (todoFormDto.getId() != null) {
            if (todoRepository.isExistById(todoFormDto.getId())) {
                throw new InvalidTodoFormDtoException("Id can not be duplicated.");
            }
            todoFormDto.setId(null);
        }
        if (todoFormDto.getTitle() == null || todoFormDto.getTitle().isEmpty()) {
            throw new InvalidTodoFormDtoException("Title is empty.");
        }
        if (todoFormDto.getDescription() == null || todoFormDto.getDescription().isEmpty()) {
            throw new InvalidTodoFormDtoException("Description is empty.");
        }
        Todo todo = new Todo(todoFormDto);
        todo.setAuthor(getCurrentUser());
        Long id = todoRepository.save(todo);
        todo.setId(id);
        return todo;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public List<Todo> findByCurrentUser() {
        String email = getCurrentUser().getEmail();
        return todoRepository.findByUserEmail(email);
    }

    public Todo findById(Optional<Long> maybeTodoId) {
        if (maybeTodoId.isEmpty()) {
            throw new InvalidTodoFormDtoException("Request with wrong id.");
        }
        Long todoId = maybeTodoId.get();
        if (!todoRepository.isExistById(todoId)) {
            throw new InvalidTodoFormDtoException("Request with wrong id.");
        }
        return todoRepository.findById(todoId);
    }

    public Todo updateTodo(Optional<TodoFormDto> maybeUpdateTodoForm) {
        if (maybeUpdateTodoForm.isEmpty()) {
            throw new InvalidTodoFormDtoException("Request with wrong form.");
        }
        TodoFormDto updateTodoFormDto = maybeUpdateTodoForm.get();
        if (updateTodoFormDto.getTitle() == null || updateTodoFormDto.getTitle().isEmpty()) {
            throw new InvalidTodoFormDtoException("Request with wrong form.");
        }
        if (updateTodoFormDto.getDescription() == null || updateTodoFormDto.getDescription().isEmpty()) {
            throw new InvalidTodoFormDtoException("Request with wrong form.");
        }

        if (todoRepository.isExistById(updateTodoFormDto.getId())) {
            Todo updateTodo = todoRepository.findById(updateTodoFormDto.getId());
            if (!updateTodo.getAuthor().equals(getCurrentUser())) {
                throw new InvalidTodoFormDtoException("Can not update todo which is not yours.");
            }
            updateTodo.setTitle(updateTodoFormDto.getTitle());
            updateTodo.setDescription(updateTodoFormDto.getDescription());
            updateTodo.setDone(updateTodoFormDto.isDone());
            updateTodo.setStartDate(updateTodoFormDto.getStartDate());
            updateTodo.setEndDate(updateTodoFormDto.getEndDate());

            todoRepository.update(updateTodo);
            return updateTodo;
        }
        throw new InvalidTodoFormDtoException("Request with wrong form.");
    }

    public void deleteById(Optional<Long> maybeTodoId) {
        if(maybeTodoId.isEmpty()) {
            throw new InvalidTodoFormDtoException("Request with wrong id.");
        }

        Long todoId = maybeTodoId.get();
        if (todoRepository.isExistById(todoId)) {
            Todo deleteTodo = todoRepository.findById(todoId);
            if (!deleteTodo.getAuthor().equals(getCurrentUser())) {
                throw new InvalidTodoFormDtoException("Can not delete todo which is not yours.");
            }
            todoRepository.delete(deleteTodo);
        }
        else {
            throw new InvalidTodoFormDtoException("Request with wrong id.");
        }
    }

    private User getCurrentUser() {
        return authenticationContext.getAuthenticatedUserHolder().getAuthenticatedUser().get();
    }
}
