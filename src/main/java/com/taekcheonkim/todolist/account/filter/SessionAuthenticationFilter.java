package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionAuthenticationFilter extends AuthenticationFilter {
    private final String attributeKeyOfAuthenticated;
    private final String attributeKeyOfAuthenticatedUserHolder;

    @Autowired
    public SessionAuthenticationFilter(AuthenticationContext authenticationContext) {
        super(authenticationContext);
        this.attributeKeyOfAuthenticated = "authentication";
        this.attributeKeyOfAuthenticatedUserHolder = "authenticatedUserHolder";
    }

    @Override
    protected void updateAuthenticationContext(HttpServletRequest request) {
        if (isNotAuthenticated(request)) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(attributeKeyOfAuthenticated, false);
            httpSession.setAttribute(attributeKeyOfAuthenticatedUserHolder, new AuthenticatedUserHolder(Optional.empty()));
        }
        Object result = request.getSession().getAttribute(attributeKeyOfAuthenticatedUserHolder);
        authenticationContext.setAuthenticatedUserHolder((AuthenticatedUserHolder) result);
    }

    private boolean isNotAuthenticated(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Object sessionAttribute = httpSession.getAttribute(attributeKeyOfAuthenticated);
        return sessionAttribute == null || !(boolean) sessionAttribute;
    }

    public String getAttributeKeyOfAuthenticate() {
        return this.attributeKeyOfAuthenticated;
    }

    public String getAttributeKeyOfAuthenticatedUserHolder() {
        return this.attributeKeyOfAuthenticatedUserHolder;
    }
}
