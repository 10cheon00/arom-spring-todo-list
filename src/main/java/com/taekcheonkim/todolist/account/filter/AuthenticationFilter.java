package com.taekcheonkim.todolist.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.exception.RequireContentCachingRequestWrapperException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

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
        if (!(request instanceof ContentCachingRequestWrapper)) {
            throw new RequireContentCachingRequestWrapperException();
        }

        getLoginDtoFromRequest(request);
        authenticateLoginDto(request);
        filterChain.doFilter(request, response);
    }

    private void getLoginDtoFromRequest(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] body = requestWrapper.getContentAsByteArray();
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
    }

    protected abstract void afterSuccessAuthentication(HttpServletRequest request);
}
