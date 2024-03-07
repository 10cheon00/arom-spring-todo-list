package com.taekcheonkim.todolist.user.authentication;

import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignInFormDto;
import com.taekcheonkim.todolist.user.repository.UserRepository;
import com.taekcheonkim.todolist.user.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticatedUserHolder authenticate(Optional<SignInFormDto> maybeSignInFormDto) {
        if(maybeSignInFormDto.isPresent()) {
            SignInFormDto signInFormDto = maybeSignInFormDto.get();
            if (userRepository.isExistByEmail(signInFormDto.getEmail())) {
                User user = userRepository.findByEmail(signInFormDto.getEmail());
                String encodedPassword = passwordEncoder.encode(signInFormDto.getPassword());
                if (encodedPassword.equals(user.getPassword())) {
                    return new AuthenticatedUserHolder(Optional.of(user));
                }
            }
        }
        return new AuthenticatedUserHolder(Optional.empty());
    }
}
