package com.taekcheonkim.todolist.user.repository;

import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.util.PasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MysqlUserRepository implements UserRepository {
    private final EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MysqlUserRepository(EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public boolean isExistByEmail(String email) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE EXISTS (SELECT 1 FROM User t WHERE t.email = :email)", User.class);
        query.setParameter("email", email);
        return query.getResultList().size() == 1;
    }
}