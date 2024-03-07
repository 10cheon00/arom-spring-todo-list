package com.taekcheonkim.todolist.user.service;

import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignInFormException;
import com.taekcheonkim.todolist.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private final SignUpFormDto signUpFormDto;
    private final User alreadyExistUser;

    public UserServiceTest() {
        String email = "test@domain.com";
        String password = "testpassword";
        String nickname = "testnickname";
        this.signUpFormDto = new SignUpFormDto(email, password, nickname);
        this.alreadyExistUser = new User(email, password, nickname);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void successSignUpWithValidUserFormDto() {
        // given
        when(userRepository.isExistByEmail(any(String.class))).thenReturn(false);
        // when
        User signInUser = userService.signUp(Optional.of(signUpFormDto));
        // then
        assertThat(signInUser.getEmail()).isEqualTo(signUpFormDto.getEmail());
    }

    @Test
    void failSignUpWhenEmailAlreadyExists() {
        // given
        when(userRepository.isExistByEmail(any(String.class))).thenReturn(true);
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signUp(Optional.of(signUpFormDto));
        }).isInstanceOf(InvalidSignInFormException.class);
    }

    @Test
    void failSignUpWhenInvalidEmail() {
        // given
        when(userRepository.isExistByEmail(any(String.class))).thenReturn(true);
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signUp(Optional.of(signUpFormDto));
        }).isInstanceOf(InvalidSignInFormException.class);
    }
}
