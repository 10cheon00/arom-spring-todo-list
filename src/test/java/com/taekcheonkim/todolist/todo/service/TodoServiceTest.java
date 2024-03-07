package com.taekcheonkim.todolist.todo.service;

import com.taekcheonkim.todolist.todo.domain.Todo;
import com.taekcheonkim.todolist.todo.dto.TodoFormDto;
import com.taekcheonkim.todolist.todo.exception.InvalidTodoFormDtoException;
import com.taekcheonkim.todolist.todo.repository.TodoRepository;
import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    private AuthenticationContext authenticationContext;
    private TodoService todoService;
    private Long savedTodoId;
    private final Todo savedTodo;
    private final List<Todo> savedTodoList;
    private final User author;

    public TodoServiceTest() {
        this.savedTodoId = 1L;
        this.savedTodo = new Todo("title", "description");
        this.savedTodo.setId(savedTodoId);
        this.author = new User("test@test.com","testpassword", "nickname");
        this.savedTodo.setAuthor(this.author);
        this.savedTodoList = new ArrayList<>();
        this.savedTodoList.add(savedTodo);
        this.authenticationContext = new AuthenticationContext();
        this.authenticationContext.setAuthenticatedUserHolder(
                new AuthenticatedUserHolder(
                        Optional.of(this.author)));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.todoService = new TodoService(todoRepository, authenticationContext);
    }

    @Test
    void successToCreateTodo() {
        // given
        String title = "title";
        String description = "description";
        TodoFormDto todoFormDto = new TodoFormDto(title, description);
        // when
        Todo todo = todoService.createTodo(Optional.of(todoFormDto));
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
        // then
        assertThatThrownBy(() -> {
            todoService.createTodo(Optional.of(invalidTodoFormDto1));
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        assertThatThrownBy(() -> {
            todoService.createTodo(Optional.of(invalidTodoFormDto2));
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
        // given
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        // when
        Todo todo = todoService.findById(Optional.of(savedTodoId));
        // then
        assertThat(todo.getId()).isEqualTo(savedTodoId);
    }

    @Test
    void failToFindByIdWithWrongId() {
        // given
        Long wrongId = 2L;
        when(todoRepository.isExistById(any(Long.class))).thenReturn(false);
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        // when
        // then
        assertThatThrownBy(() -> {
            todoService.findById(Optional.of(wrongId));
        }).isInstanceOf(InvalidTodoFormDtoException.class);
    }

    @Test
    void updateTodoById() {
        // given
        TodoFormDto updateTodoForm = new TodoFormDto("new title", "new description");
        updateTodoForm.setId(savedTodoId);
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        // when
        assertThatNoException().isThrownBy(() -> {
            Todo updatedResult = todoService.updateTodo(Optional.of(updateTodoForm));
            // then
            assertThat(updatedResult.getTitle()).isEqualTo(updateTodoForm.getTitle());
        });
    }

    @Test
    void failToUpdateTodoByInvalidTodoFormDto() {
        // given
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        // when
        TodoFormDto invalidUpdateTodoForm1 = new TodoFormDto(null, "new description");
        invalidUpdateTodoForm1.setId(savedTodoId);
        TodoFormDto invalidUpdateTodoForm2 = new TodoFormDto("new title", null);
        invalidUpdateTodoForm2.setId(savedTodoId);
        // then
        assertThatThrownBy(() -> {
            todoService.updateTodo(Optional.of(invalidUpdateTodoForm1));
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        assertThatThrownBy(() -> {
            todoService.updateTodo(Optional.of(invalidUpdateTodoForm2));
        }).isInstanceOf(InvalidTodoFormDtoException.class);
    }

    @Test
    void deleteTodoById() {
        // given
        when(todoRepository.isExistById(any(Long.class))).thenReturn(true);
        when(todoRepository.findById(any(Long.class))).thenReturn(savedTodo);
        doNothing().when(todoRepository).delete(any(Todo.class));
        // when
        // then
        assertThatNoException().isThrownBy(() -> {
            todoService.deleteById(Optional.of(savedTodoId));
        });
        verify(todoRepository, atLeastOnce()).delete(any(Todo.class));
    }

    @Test
    void failToDeleteTodoByInvalidId() {
        when(todoRepository.isExistById(any(Long.class))).thenReturn(false);
        Long wrongTodoId = 2L;
        // when
        assertThatThrownBy(() -> {
            todoService.deleteById(Optional.of(wrongTodoId));
        }).isInstanceOf(InvalidTodoFormDtoException.class);
        // then
    }
}
