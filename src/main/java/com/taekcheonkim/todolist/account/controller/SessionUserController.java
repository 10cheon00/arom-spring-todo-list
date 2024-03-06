package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;

public class SessionUserController extends UserController {
    public SessionUserController(UserService userService, AuthenticationManager authenticationManager) {
        super(userService, authenticationManager);
    }

    @PostMapping("/signin")
    public void signIn(HttpServletResponse response, LoginDto loginDto) {

    }
}
