package com.taekcheonkim.todolist.account.authentication;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
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

    public AuthenticatedUserHolder authenticate(Optional<LoginDto> maybeLoginDto) {
        if(maybeLoginDto.isPresent()) {
            LoginDto loginDto = maybeLoginDto.get();
            User user = userRepository.findByEmail(loginDto.getEmail());
            return new AuthenticatedUserHolder(Optional.of(user));
        }
        return new AuthenticatedUserHolder(Optional.empty());
    }
}
