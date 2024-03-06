package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.account.authentication.SessionAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionAuthenticationFilter extends AuthenticationFilter {

    @Autowired
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
