package com.taekcheonkim.todolist.account.filter;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationFilter {
    void authenticate(HttpServletRequest request);
}
