package com.taekcheonkim.todolist.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
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
    protected final AuthenticationContext authenticationContext;
    protected Optional<LoginDto> maybeLoginDto;
    protected AuthenticatedUserHolder authenticatedUserHolder;
    protected MultipleReadableHttpServletRequestWrapper requestWrapper;

    protected AuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationContext authenticationContext) {
        this.authenticationManager = authenticationManager;
        this.authenticationContext = authenticationContext;
    }

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        wrapRequestToMultipleReadableHttpServletRequestWrapper(request);

        if (canPassAuthentication()) {
            passAuthentication();
        } else {
            getLoginDtoFromRequest();
            authenticate();
            updateAuthenticationContext();
        }
        filterChain.doFilter(requestWrapper, response);
    }

    protected void wrapRequestToMultipleReadableHttpServletRequestWrapper(HttpServletRequest request) {
        this.requestWrapper = new MultipleReadableHttpServletRequestWrapper(request);
    }

    protected boolean canPassAuthentication() {
        return false;
    }

    protected void passAuthentication() {
    }

    protected final void getLoginDtoFromRequest() {
        try {
            byte[] body = requestWrapper.getInputStream().readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDto loginDto = objectMapper.readValue(body, LoginDto.class);
            this.maybeLoginDto = Optional.of(loginDto);
        } catch (IOException e) {
            this.maybeLoginDto = Optional.empty();
        }
    }

    protected final void authenticate() {
        authenticatedUserHolder = authenticationManager.authenticate(maybeLoginDto);
    }

    protected final void updateAuthenticationContext() {
        authenticationContext.setAuthenticatedUserHolder(authenticatedUserHolder);
        if (authenticatedUserHolder.isAuthenticated()) {
            afterSuccessAuthentication();
        } else {
            afterFailAuthentication();
        }
    }

    protected abstract void afterSuccessAuthentication();

    protected abstract void afterFailAuthentication();

    protected MultipleReadableHttpServletRequestWrapper getRequestWrapper() {
        return this.requestWrapper;
    }
}
