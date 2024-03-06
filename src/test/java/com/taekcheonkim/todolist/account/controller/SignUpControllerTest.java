package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidUserFormException;
import com.taekcheonkim.todolist.account.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SignUpControllerTest {
    private SignUpController signUpController;
    @Mock
    private UserService userService;

    private final UserFormDto userFormDto;
    private final User savedUser;
    private final SavedUserDto savedUserDto;

    public SignUpControllerTest() {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        this.savedUser = new User(this.userFormDto);
        this.savedUserDto = new SavedUserDto(email, nickname);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.signUpController = new SignUpController(userService);
    }

    @Test
    void successSignUp() {
        // given
        when(userService.signUp(any(Optional.class))).thenReturn(savedUser);
        // when
        ResponseEntity<SavedUserDto> response = signUpController.signUp(userFormDto);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedUserDto);
    }

    @Test
    void failSignUpByUserService() {
        // given
        when(userService.signUp(any(Optional.class))).thenThrow(new InvalidUserFormException(""));
        // when
        ResponseEntity<SavedUserDto> response = signUpController.signUp(userFormDto);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void failSignUpByInvalidLoginDto() {
        // given
        when(userService.signUp(any(Optional.class))).thenThrow(new InvalidUserFormException(""));
        // when
        ResponseEntity<SavedUserDto> response = signUpController.signUp(null);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
