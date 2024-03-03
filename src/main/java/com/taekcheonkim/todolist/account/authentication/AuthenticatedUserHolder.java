package com.taekcheonkim.todolist.account.authentication;

import com.taekcheonkim.todolist.account.domain.User;

import java.util.Optional;

public class AuthenticatedUserHolder {
    private Optional<User> maybeUser;

    public AuthenticatedUserHolder(Optional<User> maybeUser) {
        this.maybeUser = maybeUser;
    }

    public boolean isAuthenticated() {
        return maybeUser.isPresent();
    }

    public Optional<User> getAuthenticatedUser() {
        return maybeUser;
    }
}
