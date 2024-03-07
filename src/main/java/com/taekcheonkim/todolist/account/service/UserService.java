package com.taekcheonkim.todolist.account.service;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SignUpFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidUserFormException;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUp(Optional<SignUpFormDto> maybeUserFormDto) throws InvalidUserFormException {
        if (maybeUserFormDto.isEmpty()) {
            throw new InvalidUserFormException("User form is empty.");
        }

        SignUpFormDto signUpFormDto = maybeUserFormDto.get();
        if (userRepository.isExistByEmail(signUpFormDto.getEmail())) {
            throw new InvalidUserFormException("Already exists email.");
        }

        User user = new User(signUpFormDto);
        userRepository.save(user);
        return user;
    }
}
