package com.taekcheonkim.todolist.account.repository;

import com.taekcheonkim.todolist.account.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    User findByEmail(String email);
}