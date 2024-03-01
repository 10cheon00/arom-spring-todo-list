package com.taekcheonkim.todolist.repository;

import com.taekcheonkim.todolist.domain.Todo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RepositoryTest {
    private final TodoRepository todoRepository;

    @Autowired
    public RepositoryTest(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Test
    public void CreateTodo(){
        // given
        // when
        Todo todo = new Todo();
        todoRepository.save(todo);
        List<Todo> result = todoRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(1);
    }
    
    @Test
    public void ReadAllTodo() {
        // given
        Todo todo1 = new Todo();
        Todo todo2 = new Todo();
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        // when
        List<Todo> result = todoRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void ReadTodoById() {
        // given
        Todo todo = new Todo("Todo 1", "");
        todoRepository.save(todo);
        // when
        Todo result1 = todoRepository.findById(todo.getId());
        // then
        assertThat(result1.getId()).isEqualTo(todo.getId());
    }

    @Test
    public void ReadTodoByEmail() {
        // given
        // when
        // then
    }
    
    @Test
    public void UpdateTodo() {
        // given
        Todo todo = new Todo();
        todoRepository.save(todo);
        // when
        String newTitle = "New Todo";
        todo.setTitle(newTitle);
        todoRepository.update(todo);
        Todo updatedTodo = todoRepository.findById(todo.getId());
        // then
        assertThat(updatedTodo.getTitle()).isEqualTo(todo.getTitle());
    }

    @Test
    public void DeleteTodo() {
        // given
        Todo todo = new Todo();
        todoRepository.save(todo);
        // when
        todoRepository.delete(todo);
        List<Todo> result = todoRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void ClearRepository() {
        // given
        Todo todo1 = new Todo();
        Todo todo2 = new Todo();
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        // when
        todoRepository.deleteAll();
        List<Todo> result = todoRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(0);
    }
}
