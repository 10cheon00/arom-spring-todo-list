package com.taekcheonkim.todolist.todo.service;

import com.taekcheonkim.todolist.todo.domain.Todo;
import com.taekcheonkim.todolist.todo.dto.TodoFormDto;
import com.taekcheonkim.todolist.todo.exception.InvalidTodoFormDtoException;
import com.taekcheonkim.todolist.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    private TodoService todoService;
    private Long savedTodoId;
    private final Todo savedTodo;
    private final List<Todo> savedTodoList;

    public TodoServiceTest() {
        this.savedTodoId = 1L;
        this.savedTodo = new Todo("title", "description");
        this.savedTodo.setId(savedTodoId);
        this.savedTodoList = new ArrayList<>();
        this.savedTodoList.add(savedTodo);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.todoService = new TodoService(todoRepository);
    }

    @Test
    void successToCreateTodo() {
        // given
        String title = "title";
        String description = "description";
        TodoFormDto todoFormDto = new TodoFormDto(title, description);
        // when
        Todo todo = todoService.save(todoFormDto);
        // then
        assertThat(todo.getTitle()).isEqualTo(todoFormDto.getTitle());
        assertThat(todo.getDescription()).isEqualTo(todoFormDto.getDescription());
        assertThat(todo.getId()).isInstanceOf(Long.class);
    }

    @Test
    void failToCreateTodoWithInvalidTodoFormDto() {
        // given
        String title = "title";
        String description = "description";
        // when
        TodoFormDto invalidTodoFormDto1 = new TodoFormDto(null, description);
        TodoFormDto invalidTodoFormDto2 = new TodoFormDto(title, null);
        TodoFormDto invalidTodoFormDto3 = new TodoFormDto(title, description);
        // then
        assertThatThrownBy(() -> {
            todoService.save(invalidTodoFormDto1);
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        assertThatThrownBy(() -> {
            todoService.save(invalidTodoFormDto2);
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        assertThatThrownBy(() -> {
            todoService.save(invalidTodoFormDto3);
        }).isInstanceOf(InvalidTodoFormDtoException.class);
    }

    @Test
    void findAllTodo() {
        // given
        when(todoRepository.findAll()).thenReturn(savedTodoList);
        // when
        List<Todo> result = todoService.findAll();
        // then
        assertThat(result.size()).isOne();
    }

    @Test
    void findById() {
        Long todoId = 1L;
        // given
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        // when
        Todo result = todoService.findById(todoId);
        // then
        assertThat(result.getId()).isEqualTo(todoId);
    }

    @Test
    void updateTodoById() {
        // given
        TodoFormDto updateTodoForm = new TodoFormDto("new title", "new description");
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        // when
        Todo updatedResult = todoService.update(updateTodoForm);
        Todo result = todoService.findById(updateTodoForm.getId());
        // then
        assertThat(updatedResult.getTitle()).isEqualTo(result.getTitle());
        assertThat(updatedResult.getDescription()).isEqualTo(result.getDescription());
    }

    @Test
    void failToUpdateTodoByInvalidTodoFormDto() {
        // given
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        // when
        TodoFormDto invalidUpdateTodoForm1 = new TodoFormDto(null, "new description");
        invalidUpdateTodoForm1.setId(savedTodoId);
        TodoFormDto invalidUpdateTodoForm2 = new TodoFormDto("new title", null);
        invalidUpdateTodoForm2.setId(savedTodoId);
        TodoFormDto invalidUpdateTodoForm3 = new TodoFormDto("new title", "new description");
        // then
        assertThatThrownBy(() -> {
            todoService.update(invalidUpdateTodoForm1);
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        assertThatThrownBy(() -> {
            todoService.update(invalidUpdateTodoForm2);
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        assertThatThrownBy(() -> {
            todoService.update(invalidUpdateTodoForm3);
        }).isInstanceOf(InvalidTodoFormDtoException.class);
    }

    @Test
    void deleteTodoById() {
        // given
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        when(todoRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(todoRepository).delete(any(Todo.class));
        // when
        assertThatNoException().isThrownBy(() -> {
            todoService.deleteById(savedTodoId);
        });
        // then
        List<Todo> result = todoService.findAll();
        assertThat(result.size()).isZero();
    }

    @Test
    void failToDeleteTodoByInvalidId() {
        when(todoRepository.isExistById(any(Long.class))).thenReturn(false);
        when(todoRepository.findAll()).thenReturn(savedTodoList);
        Long wrongTodoId = 2L;
        // when
        todoService.deleteById(wrongTodoId);
        // then
        List<Todo> result = todoService.findAll();
        assertThat(result.size()).isOne();
    }
}
