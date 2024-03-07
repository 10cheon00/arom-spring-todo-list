package com.taekcheonkim.todolist.user.repository;

import com.taekcheonkim.todolist.user.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    User findByEmail(String email);
    boolean isExistByEmail(String email);
}