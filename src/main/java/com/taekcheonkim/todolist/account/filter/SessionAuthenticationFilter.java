package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionAuthenticationFilter extends AuthenticationFilter {
    private final String attributeKeyOfAuthenticated;
    private final String attributeKeyOfAuthenticatedUserHolder;

    @Autowired
    public SessionAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationContext authenticationContext) {
        super(authenticationManager, authenticationContext);
        this.attributeKeyOfAuthenticated = "authentication";
        this.attributeKeyOfAuthenticatedUserHolder = "authenticatedUserHolder";
    }

    @Override
    protected boolean canPassAuthentication() {
        return isSessionAttributeTrue();
    }

    @Override
    protected void passAuthentication() {
        HttpSession httpSession = getRequestWrapper().getSession();
        AuthenticatedUserHolder userHolder = (AuthenticatedUserHolder) httpSession.getAttribute(attributeKeyOfAuthenticatedUserHolder);
        authenticationContext.setAuthenticatedUserHolder(userHolder);
    }

    @Override
    protected void afterSuccessAuthentication() {
        HttpSession httpSession = getRequestWrapper().getSession();
        httpSession.setAttribute(attributeKeyOfAuthenticated, true);
        httpSession.setAttribute(attributeKeyOfAuthenticatedUserHolder, authenticatedUserHolder);
    }

    @Override
    protected void afterFailAuthentication() {
        HttpSession httpSession = getRequestWrapper().getSession();
        httpSession.setAttribute(attributeKeyOfAuthenticated, false);
        httpSession.setAttribute(attributeKeyOfAuthenticatedUserHolder, null);
    }

    private boolean isSessionAttributeTrue() {
        HttpSession httpSession = getRequestWrapper().getSession();
        Object sessionAttribute = httpSession.getAttribute(attributeKeyOfAuthenticated);
        return sessionAttribute != null && (boolean) sessionAttribute;
    }

    public String getAttributeKeyOfAuthenticate() {
        return this.attributeKeyOfAuthenticated;
    }

    public String getAttributeKeyOfAuthenticatedUserHolder() {
        return this.attributeKeyOfAuthenticatedUserHolder;
    }
}
