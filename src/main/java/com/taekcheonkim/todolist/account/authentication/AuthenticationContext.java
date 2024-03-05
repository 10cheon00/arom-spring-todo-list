package com.taekcheonkim.todolist.account.authentication;

import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public class AuthenticationContext {
    private AuthenticatedUserHolder authenticatedUserHolder;

    public AuthenticatedUserHolder getAuthenticatedUserHolder() {
        return this.authenticatedUserHolder;
    }

    public void setAuthenticatedUserHolder(AuthenticatedUserHolder authenticatedUserHolder) {
        this.authenticatedUserHolder = authenticatedUserHolder;
    }
}
