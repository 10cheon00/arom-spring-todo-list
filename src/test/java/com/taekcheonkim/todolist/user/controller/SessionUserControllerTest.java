package com.taekcheonkim.todolist.user.controller;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.user.authentication.SessionAttributes;
import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignInFormDto;
import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignUpFormException;
import com.taekcheonkim.todolist.user.service.UserService;
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

    private final SignUpFormDto signUpFormDto;
    private final SignInFormDto signInFormDto;
    private final SignInFormDto invalidSignInFormDto;
    private final User savedUser;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession httpSession;

    public SessionUserControllerTest() {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.signUpFormDto = new SignUpFormDto(email, password, nickname);
        this.signInFormDto = new SignInFormDto(email, password);
        this.invalidSignInFormDto = new SignInFormDto(email, password);
        this.savedUser = new User(this.signUpFormDto);

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
            sessionUserController.signIn(signInFormDto, request, response);
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
        assertThatThrownBy(() -> sessionUserController.signIn(invalidSignInFormDto, request, response)).isInstanceOf(InvalidSignUpFormException.class);
    }

    @Test
    void failSignInWithEmptyLoginDto() {
        // given
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        // then
        assertThatThrownBy(() -> sessionUserController.signIn(null, request, response)).isInstanceOf(InvalidSignUpFormException.class);
    }
}
