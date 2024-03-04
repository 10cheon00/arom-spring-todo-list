package com.taekcheonkim.todolist.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.util.MultipleReadableHttpServletRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public abstract class AuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    protected Optional<LoginDto> maybeLoginDto;
    protected AuthenticatedUserHolder authenticatedUserHolder;

    protected AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getLoginDtoFromRequest(request);
        authenticateLoginDto(request);
        filterChain.doFilter(request, response);
    }

    private void getLoginDtoFromRequest(HttpServletRequest request) {
        try {
            MultipleReadableHttpServletRequestWrapper requestWrapper = new MultipleReadableHttpServletRequestWrapper(request);
            byte[] body = requestWrapper.getInputStream().readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDto loginDto = objectMapper.readValue(body, LoginDto.class);
            this.maybeLoginDto = Optional.of(loginDto);
        } catch (IOException e) {
            this.maybeLoginDto = Optional.empty();
        }
    }

    private void authenticateLoginDto(HttpServletRequest request) {
        authenticatedUserHolder = authenticationManager.authenticate(maybeLoginDto);
        if (authenticatedUserHolder.isAuthenticated()) {
            afterSuccessAuthentication(request);
        }
        else {
            afterFailAuthentication(request);
        }
    }

    protected abstract void afterSuccessAuthentication(HttpServletRequest request);
    protected abstract void afterFailAuthentication(HttpServletRequest request);
}
