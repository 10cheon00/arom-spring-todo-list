package com.taekcheonkim.todolist.account.authorization;

import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequestScope
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final AuthenticationContext authenticationContext;

    public AuthorizationInterceptor(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        System.out.println(handlerMethod.getBean().getClass());
        return true;
    }
}
