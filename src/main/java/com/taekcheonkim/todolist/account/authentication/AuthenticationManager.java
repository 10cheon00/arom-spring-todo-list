package com.taekcheonkim.todolist.account.authentication;

import com.taekcheonkim.todolist.account.dto.LoginDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface AuthenticationManager {
    void authenticate(Optional<LoginDto> maybeLoginDto);
}
