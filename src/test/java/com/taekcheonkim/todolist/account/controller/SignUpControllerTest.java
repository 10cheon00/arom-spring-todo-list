package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidUserFormException;
import com.taekcheonkim.todolist.account.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SignUpControllerTest {
    private SignUpController signUpController;
    @Mock
    private UserService userService;
    private final UserFormDto userFormDto;
    private final User savedUser;

    public SignUpControllerTest() {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        this.savedUser = new User(this.userFormDto);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.signUpController = new SignUpController(userService);
    }

    @Test
    void successSignUp() {
        // given
        when(userService.signUp(any(UserFormDto.class))).thenReturn(savedUser);
        // when
        User result = signUpController.signUp(userFormDto);
        // then
        assertThat(result).isEqualTo(savedUser);
    }

    @Test
    void failSignUpByUserService() {
        // given
        when(userService.signUp(any(UserFormDto.class))).thenThrow(new InvalidUserFormException(""));
        // when
        // then
        assertThatThrownBy(() -> {
            signUpController.signUp(userFormDto);
        }).isInstanceOf(InvalidUserFormException.class);
    }

    @Test
    void failSignUpByInvalidLoginDto() {
        // given
        when(userService.signUp(any(UserFormDto.class))).thenThrow(new InvalidUserFormException(""));
        // when
        // then
        assertThatThrownBy(() -> {
            signUpController.signUp(null);
        }).isInstanceOf(InvalidUserFormException.class);
    }
    
}
