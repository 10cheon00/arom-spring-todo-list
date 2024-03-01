package com.taekcheonkim.todolist.repository;

import com.taekcheonkim.todolist.domain.Todo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MysqlTodoRepository implements TodoRepository {
    private final EntityManager entityManager;

    @Autowired
    public MysqlTodoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Todo save(Todo todo) {
        return null;
    }

    @Override
    public List<Todo> findAll() {
        return null;
    }

    @Override
    public Todo findByTitle(String title) {
        return null;
    }

    @Override
    public void delete(Todo todo) {

    }

    @Override
    public void clear() {

    }
}
