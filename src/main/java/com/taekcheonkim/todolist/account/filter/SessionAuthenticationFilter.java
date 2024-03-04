package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionAuthenticationFilter extends AuthenticationFilter {
    private final String sessionAttribute;

    @Autowired
    public SessionAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.sessionAttribute = "authentication";
    }

    @Override
    protected void afterSuccessAuthentication(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(sessionAttribute, true);
    }
}
