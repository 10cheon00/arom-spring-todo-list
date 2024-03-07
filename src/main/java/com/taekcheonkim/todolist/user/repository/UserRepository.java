package com.taekcheonkim.todolist.user.repository;

import com.taekcheonkim.todolist.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    Optional<User> findByEmail(String email);
    boolean isExistByEmail(String email);
}