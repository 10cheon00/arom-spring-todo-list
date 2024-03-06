package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping(UserController.UriPath)
public class UserController {
    public final static String UriPath = "/users";
    public final static String SignUpPath = "/signup";
    public final static String SignInPath = "/signin";

    private final UserService userService;
    protected final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(SignUpPath)
    public ResponseEntity<SavedUserDto> signUp(@RequestBody UserFormDto userFormDto) {
        User savedUser = userService.signUp(Optional.ofNullable(userFormDto));
        SavedUserDto savedUserDto = new SavedUserDto(savedUser.getEmail(), savedUser.getNickname());
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }
}
