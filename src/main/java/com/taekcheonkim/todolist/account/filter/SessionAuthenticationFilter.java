package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionAuthenticationFilter extends AuthenticationFilter {
    private final String sessionAttributeKey;

    @Autowired
    public SessionAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.sessionAttributeKey = "authentication";
    }

    @Override
    protected void afterSuccessAuthentication(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(sessionAttributeKey, true);
    }

    @Override
    protected void afterFailAuthentication(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Object sessionAttribute = httpSession.getAttribute(sessionAttributeKey);
        boolean wasNotAuthenticated = sessionAttribute == null || !(boolean)sessionAttribute;
        if (wasNotAuthenticated) {
            httpSession.setAttribute(sessionAttributeKey, false);
        }
    }
}
