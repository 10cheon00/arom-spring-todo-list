package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.exception.InvalidLoginFormException;
import com.taekcheonkim.todolist.account.authentication.SessionAttributes;
import com.taekcheonkim.todolist.account.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Optional;

public class SessionUserController extends UserController {
    public SessionUserController(UserService userService, AuthenticationManager authenticationManager) {
        super(userService, authenticationManager);
    }

    @PostMapping("/signin")
    public void signIn(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.ofNullable(loginDto));
        if (authenticatedUserHolder.isAuthenticated()) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(SessionAttributes.Authenticated, true);
            httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, authenticatedUserHolder);
            response.sendRedirect("/");
        }
        else {
            throw new InvalidLoginFormException();
        }
    }
}
