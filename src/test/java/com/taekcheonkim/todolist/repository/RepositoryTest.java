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
    public void ReadTodo() {
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
    public void ReadSpecificTodo() {
        // given
        Todo todo1 = new Todo("Title1", "");
        Todo todo2 = new Todo("Title2", "");
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        // when
        Todo result1 = todoRepository.findByTitle(todo1.getTitle());
        Todo result2 = todoRepository.findByTitle(todo2.getTitle());
        // then
        assertThat(result1.getTitle()).isEqualTo(todo1.getTitle());
        assertThat(result2.getTitle()).isEqualTo(todo2.getTitle());
    }
    
    @Test
    public void UpdateTodo() {
        // given
        Todo todo = new Todo();
        todoRepository.save(todo);
        // when
        String newTitle = "New Todo";
        todo.setTitle(newTitle);
        todoRepository.save(todo);
        Todo updatedTodo = todoRepository.findByTitle(todo.getTitle());
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
