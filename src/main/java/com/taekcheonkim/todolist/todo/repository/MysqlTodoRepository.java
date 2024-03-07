package com.taekcheonkim.todolist.todo.repository;

import com.taekcheonkim.todolist.todo.domain.Todo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
    public Long save(Todo todo) {
        entityManager.persist(todo);
        entityManager.flush();
        return todo.getId();
    }

    @Override
    public List<Todo> findAll() {
        return entityManager.createQuery("SELECT t FROM Todo t", Todo.class).getResultList();
    }

    @Override
    public List<Todo> findByUserEmail(String email) {
        TypedQuery<Todo> query = entityManager.createQuery("SELECT t FROM Todo t WHERE t.author.email = :email", Todo.class);
        query.setParameter("email", email);
        return query.getResultList();
    }

    @Override
    public Todo findById(Long id) {
        TypedQuery<Todo> query = entityManager.createQuery("SELECT t FROM Todo t WHERE t.id = :id", Todo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public boolean isExistById(Long id) {
        Query query = entityManager.createQuery("SELECT t FROM Todo t WHERE EXISTS (SELECT 1 FROM User t WHERE t.id = :id)", Todo.class);
        query.setParameter("id", id);
        return query.getResultList().size() == 1;
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