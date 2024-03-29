package com.taekcheonkim.todolist.user.controller;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignInFormDto;
import com.taekcheonkim.todolist.user.dto.SavedUserDto;
import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignInFormException;
import com.taekcheonkim.todolist.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    private final AuthenticatedUserHolder authenticatedUserHolder;
    private final AuthenticatedUserHolder notAuthenticatedUserHolder;

    private final SignUpFormDto signUpFormDto;
    private final SignInFormDto signInFormDto;
    private final SignInFormDto invalidSignInFormDto;
    private final User savedUser;
    private final SavedUserDto savedUserDto;

    public UserControllerTest() {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.signUpFormDto = new SignUpFormDto(email, password, nickname);
        this.signInFormDto = new SignInFormDto(email, password);
        this.invalidSignInFormDto = new SignInFormDto(email, password);
        this.savedUser = new User(this.signUpFormDto);
        this.savedUserDto = new SavedUserDto(email, nickname);

        this.authenticatedUserHolder = new AuthenticatedUserHolder(Optional.of(savedUser));
        this.notAuthenticatedUserHolder = new AuthenticatedUserHolder(Optional.empty());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.userController = new UserController(userService, authenticationManager);
    }

    @Test
    void successSignUp() {
        // given
        when(userService.signUp(any(Optional.class))).thenReturn(savedUser);
        // when
        ResponseEntity<SavedUserDto> response = userController.signUp(signUpFormDto);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedUserDto);
    }

    @Test
    void failSignUpByUserService() {
        // given
        when(userService.signUp(any(Optional.class))).thenThrow(new InvalidSignInFormException(""));
        // when
        // then
        assertThatThrownBy(() -> userController.signUp(signUpFormDto)).isInstanceOf(InvalidSignInFormException.class);
    }

    @Test
    void failSignUpByInvalidSignUpFormDto() {
        // given
        when(userService.signUp(any(Optional.class))).thenThrow(new InvalidSignInFormException(""));
        // when
        // then
        assertThatThrownBy(() -> userController.signUp(null)).isInstanceOf(InvalidSignInFormException.class);
    }
}
