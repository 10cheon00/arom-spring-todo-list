package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.repository.UserRepository;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}
