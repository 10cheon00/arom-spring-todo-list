package com.taekcheonkim.todolist.account.authentication;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SignInFormDto;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationManager {
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticatedUserHolder authenticate(Optional<SignInFormDto> maybeLoginDto) {
        if(maybeLoginDto.isPresent()) {
            SignInFormDto signInFormDto = maybeLoginDto.get();
            if (userRepository.isExistByEmail(signInFormDto.getEmail())) {
                Optional<User> maybeUser = userRepository.findByEmail(signInFormDto.getEmail());
                return new AuthenticatedUserHolder(maybeUser);
            }
        }
        return new AuthenticatedUserHolder(Optional.empty());
    }
}
