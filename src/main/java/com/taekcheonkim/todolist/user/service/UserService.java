package com.taekcheonkim.todolist.user.service;

import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignInFormException;
import com.taekcheonkim.todolist.user.repository.UserRepository;
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

    public User signUp(Optional<SignUpFormDto> maybeUserFormDto) throws InvalidSignInFormException {
        if (maybeUserFormDto.isEmpty()) {
            throw new InvalidSignInFormException("User form is empty.");
        }

        SignUpFormDto signUpFormDto = maybeUserFormDto.get();
        if (userRepository.isExistByEmail(signUpFormDto.getEmail())) {
            throw new InvalidSignInFormException("Already exists email.");
        }

        User user = new User(signUpFormDto);
        userRepository.save(user);
        return user;
    }
}
