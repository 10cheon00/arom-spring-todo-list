package com.taekcheonkim.todolist.account.repository;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.exception.DuplicatedUserEmailException;
import com.taekcheonkim.todolist.account.exception.DuplicatedUserNicknameException;
import com.taekcheonkim.todolist.account.util.PasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void save(User user) throws IllegalArgumentException {
        if (isExistEmail(user.getEmail())) {
            throw new DuplicatedUserEmailException();
        }
        if (isExistNickname(user.getNickname())) {
            throw new DuplicatedUserNicknameException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    private boolean isExistEmail(String email) {
        Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult().equals(1L);
    }

    private boolean isExistNickname(String nickname) {
        Query query = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.nickname = :nickname", User.class);
        query.setParameter("nickname", nickname);
        return query.getSingleResult().equals(1L);
    }
}