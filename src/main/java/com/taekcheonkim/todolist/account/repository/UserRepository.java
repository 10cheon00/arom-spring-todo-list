package com.taekcheonkim.todolist.account.repository;

import com.taekcheonkim.todolist.account.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    Optional<User> findByEmail(String email);
}