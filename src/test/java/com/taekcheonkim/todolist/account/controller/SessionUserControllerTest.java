package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.authentication.SessionAttributes;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidLoginFormException;
import com.taekcheonkim.todolist.account.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

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

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession httpSession;

    public SessionUserControllerTest() {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        this.loginDto = new LoginDto(email, password);
        this.invalidLoginDto = new LoginDto(email, password);
        this.savedUser = new User(this.userFormDto);

        this.authenticatedUserHolder = new AuthenticatedUserHolder(Optional.of(savedUser));
        this.notAuthenticatedUserHolder = new AuthenticatedUserHolder(Optional.empty());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.httpSession = new MockHttpSession();
        this.request.setSession(this.httpSession);
        this.sessionUserController = new SessionUserController(userService, authenticationManager);
    }


    @Test
    void successSignIn() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(authenticatedUserHolder);
        // when
        // then
        assertThatNoException().isThrownBy(() -> {
            sessionUserController.signIn(loginDto, request, response);
        });
        assertThat(response.getRedirectedUrl()).isNotEmpty();
        assertThat(httpSession.getAttribute(SessionAttributes.Authenticated)).isEqualTo(true);
        AuthenticatedUserHolder result = (AuthenticatedUserHolder) httpSession.getAttribute(SessionAttributes.AuthenticatedUserHolder);
        assertThat(result.isAuthenticated()).isEqualTo(true);
    }

    @Test
    void failSignInWithInvalidLoginDto() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        // then
        assertThatThrownBy(() -> sessionUserController.signIn(invalidLoginDto, request, response)).isInstanceOf(InvalidLoginFormException.class);
    }

    @Test
    void failSignInWithEmptyLoginDto() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        // then
        assertThatThrownBy(() -> sessionUserController.signIn(null, request, response)).isInstanceOf(InvalidLoginFormException.class);
    }
}
