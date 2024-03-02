package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.exception.InvalidCredentialException;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import com.taekcheonkim.todolist.account.util.PasswordEncoder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationFilterTest {
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpSession httpSession;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private final AuthenticationFilter authenticationFilter;

    private final String authenticationAttribute;
    private final LoginDto loginDto;
    private final User user;

    public AuthenticationFilterTest() {
        this.authenticationFilter = new AuthenticationFilter(userRepository);
        authenticationAttribute = "authenticate";
        String email = "user1@test.com";
        String password = "password";
        String nickname = "nickname";
        this.loginDto = new LoginDto(email, password);
        this.user = new User(email, password, nickname);
    }

    @BeforeEach
    void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    void passWithNoCredentialAndNoSession() throws ServletException, IOException {
        // given
        // when
        when(httpServletRequest.getParameter("username")).thenReturn(null);
        when(httpServletRequest.getParameter("password")).thenReturn(null);
        when(httpServletRequest.getSession()).thenReturn(null);
        // then
        verify(httpServletRequest, atLeastOnce()).getSession();
        verify(filterChain, atLeastOnce()).doFilter(httpServletRequest, httpServletResponse);
        assertThatNoException().isThrownBy(() -> {
            authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        });
    }

    @Test
    void passByExistSession() throws ServletException, IOException {
        // given
        // when
        when(httpSession.getAttribute(authenticationAttribute)).thenReturn(true);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        // then
        verify(httpServletRequest, atLeastOnce()).getSession();
        verify(httpSession, atLeastOnce()).getAttribute(authenticationAttribute);
        verify(filterChain, atLeastOnce()).doFilter(httpServletRequest, httpServletResponse);
        assertThatNoException().isThrownBy(() -> {
            authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        });
    }

    @Test
    void authenticateWithValidCredentialWhenNoSession() throws ServletException, IOException {
        // given
        // when
        when(httpServletRequest.getSession()).thenReturn(null);
        when(httpServletRequest.getParameter("username")).thenReturn(loginDto.getEmail());
        when(httpServletRequest.getParameter("password")).thenReturn(loginDto.getPassword());
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(user);
        when(passwordEncoder.encode(loginDto.getPassword())).thenReturn(user.getPassword());
        // then
        verify(httpServletRequest, atLeastOnce()).getSession();
        verify(httpServletRequest, atLeastOnce()).getParameter("username");
        verify(httpServletRequest, atLeastOnce()).getParameter("password");
        verify(userRepository, atLeastOnce()).findByEmail(loginDto.getEmail());
        verify(passwordEncoder, atLeastOnce()).encode(loginDto.getPassword());
        verify(filterChain, atLeastOnce()).doFilter(httpServletRequest, httpServletResponse);
        assertThatNoException().isThrownBy(() -> {
            authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        });
    }

    @Test
    void failAuthenticationWithNoUsernameWhenNoSession() throws ServletException, IOException {
        // given
        // when
        when(httpServletRequest.getSession()).thenReturn(null);
        when(httpServletRequest.getParameter("username")).thenReturn(loginDto.getEmail());
        when(httpServletRequest.getParameter("password")).thenReturn(null);
        // then
        verify(httpServletRequest, atLeastOnce()).getSession();
        verify(httpServletRequest, atLeastOnce()).getParameter("username");
        verify(filterChain, never()).doFilter(httpServletRequest, httpServletResponse);
        assertThatThrownBy(() -> {
            authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        }).isInstanceOf(InvalidCredentialException.class);
    }

    @Test
    void failAuthenticationWithNoPasswordWhenNoSession() throws ServletException, IOException {
        // given
        // when
        when(httpServletRequest.getSession()).thenReturn(null);
        when(httpServletRequest.getParameter("username")).thenReturn(loginDto.getEmail());
        when(httpServletRequest.getParameter("password")).thenReturn(null);
        // then
        verify(httpServletRequest, atLeastOnce()).getSession();
        verify(httpServletRequest, atLeastOnce()).getParameter("username");
        verify(httpServletRequest, atLeastOnce()).getParameter("password");
        verify(filterChain, never()).doFilter(httpServletRequest, httpServletResponse);
        assertThatThrownBy(() -> {
            authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        }).isInstanceOf(InvalidCredentialException.class);
    }

    @Test
    void failAuthenticationWithInvalidLoginDtoWhenNoSession() throws ServletException, IOException {
        // given
        // when
        when(httpServletRequest.getSession()).thenReturn(null);
        when(httpServletRequest.getParameter("username")).thenReturn(loginDto.getEmail());
        when(httpServletRequest.getParameter("password")).thenReturn(loginDto.getPassword());
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(null);
        // then
        verify(httpServletRequest, atLeastOnce()).getSession();
        verify(httpServletRequest, atLeastOnce()).getParameter("username");
        verify(httpServletRequest, atLeastOnce()).getParameter("password");
        verify(userRepository, atLeastOnce()).findByEmail(loginDto.getEmail());
        verify(filterChain, never()).doFilter(httpServletRequest, httpServletResponse);
        assertThatThrownBy(() -> {
            authenticationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
        }).isInstanceOf(InvalidCredentialException.class);
    }
}
