package com.taekcheonkim.todolist.user.filter;

import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public abstract class AuthenticationFilter extends OncePerRequestFilter {
    protected final AuthenticationContext authenticationContext;

    protected AuthenticationFilter(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        updateAuthenticationContext(request);
        filterChain.doFilter(request, response);
    }

    protected abstract void updateAuthenticationContext(HttpServletRequest request);
}
