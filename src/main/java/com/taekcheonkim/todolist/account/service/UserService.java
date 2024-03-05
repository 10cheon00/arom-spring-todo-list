package com.taekcheonkim.todolist.account.service;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User signIn(UserFormDto userFormDto) {
        return null;
    }
}
