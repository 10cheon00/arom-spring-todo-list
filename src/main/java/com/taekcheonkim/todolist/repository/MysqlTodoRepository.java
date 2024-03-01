package com.taekcheonkim.todolist.repository;

import com.taekcheonkim.todolist.domain.Todo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    public void save(Todo todo) {
        entityManager.persist(todo);
    }

    @Override
    public List<Todo> findAll() {
        return entityManager.createQuery("SELECT t FROM Todo t", Todo.class).getResultList();
    }

    @Override
    public Todo findById(Long id) {
        TypedQuery<Todo> query = entityManager.createQuery("SELECT t FROM Todo t WHERE t.id = :id", Todo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void update(Todo todo) {
        entityManager.merge(todo);
    }

    @Override
    public void delete(Todo todo) {
        entityManager.remove(todo);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Todo t").executeUpdate();
    }
}