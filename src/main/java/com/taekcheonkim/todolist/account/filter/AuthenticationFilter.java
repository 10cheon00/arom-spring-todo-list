package com.taekcheonkim.todolist.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
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
    protected final AuthenticationContext authenticationContext;
    protected Optional<LoginDto> maybeLoginDto;
    protected MultipleReadableHttpServletRequestWrapper requestWrapper;

    protected AuthenticationFilter(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        updateAuthenticationContext(request);
        filterChain.doFilter(request, response);
    }

    // todo: 나중에 이 코드 컨트롤러에서 쓰기
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

    protected abstract void updateAuthenticationContext(HttpServletRequest request);
}
