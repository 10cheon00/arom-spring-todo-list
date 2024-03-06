package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidUserFormException;
import com.taekcheonkim.todolist.account.service.UserService;
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

    private final UserFormDto userFormDto;
    private final LoginDto loginDto;
    private final LoginDto invalidLoginDto;
    private final User savedUser;
    private final SavedUserDto savedUserDto;

    public UserControllerTest() {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        this.loginDto = new LoginDto(email, password);
        this.invalidLoginDto = new LoginDto(email, password);
        this.savedUser = new User(this.userFormDto);
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
        ResponseEntity<SavedUserDto> response = userController.signUp(userFormDto);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedUserDto);
    }

    @Test
    void failSignUpByUserService() {
        // given
        when(userService.signUp(any(Optional.class))).thenThrow(new InvalidUserFormException(""));
        // when
        // then
        assertThatThrownBy(() -> userController.signUp(userFormDto)).isInstanceOf(InvalidUserFormException.class);
    }

    @Test
    void failSignUpByInvalidLoginDto() {
        // given
        when(userService.signUp(any(Optional.class))).thenThrow(new InvalidUserFormException(""));
        // when
        // then
        assertThatThrownBy(() -> userController.signUp(null)).isInstanceOf(InvalidUserFormException.class);
    }
}