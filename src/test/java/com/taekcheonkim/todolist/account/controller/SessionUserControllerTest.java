package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidLoginFormException;
import com.taekcheonkim.todolist.account.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SessionUserControllerTest {
    private SessionUserController sessionUserController;
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

    private MockHttpServletResponse response;

    public SessionUserControllerTest() {
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
        this.response = new MockHttpServletResponse();
    }


    @Test
    void successSignIn() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(authenticatedUserHolder);
        // when
        // then
        assertThatNoException().isThrownBy(() -> {
            sessionUserController.signIn(response, loginDto);
        });
        assertThat(response.getRedirectedUrl()).isNotEmpty();
    }

    @Test
    void failSignInWithInvalidLoginDto() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        // then
        assertThatThrownBy(() -> sessionUserController.signIn(response, invalidLoginDto)).isInstanceOf(InvalidLoginFormException.class);
    }

    @Test
    void failSignInWithEmptyLoginDto() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        // then
        assertThatThrownBy(() -> sessionUserController.signIn(response, null)).isInstanceOf(InvalidLoginFormException.class);
    }
}
