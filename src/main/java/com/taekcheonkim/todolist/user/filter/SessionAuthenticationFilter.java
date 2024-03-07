package com.taekcheonkim.todolist.user.filter;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.authentication.SessionAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class SessionAuthenticationFilter extends AuthenticationFilter {
    public SessionAuthenticationFilter(AuthenticationContext authenticationContext) {
        super(authenticationContext);
    }

    @Override
    protected void updateAuthenticationContext(HttpServletRequest request) {
        if (isNotAuthenticated(request)) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(SessionAttributes.Authenticated, false);
            httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, new AuthenticatedUserHolder(Optional.empty()));
        }
        Object result = request.getSession().getAttribute(SessionAttributes.AuthenticatedUserHolder);
        authenticationContext.setAuthenticatedUserHolder((AuthenticatedUserHolder) result);
    }

    private boolean isNotAuthenticated(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Object sessionAttribute = httpSession.getAttribute(SessionAttributes.Authenticated);
        return sessionAttribute == null || !(boolean) sessionAttribute;
    }
}
