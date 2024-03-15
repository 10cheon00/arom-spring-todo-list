package com.taekcheonkim.todolist.user.controller;

import com.taekcheonkim.todolist.user.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SavedUserDto;
import com.taekcheonkim.todolist.user.dto.SignUpFormDto;
import com.taekcheonkim.todolist.user.service.UserService;
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
    public ResponseEntity<SavedUserDto> signUp(@RequestBody SignUpFormDto signUpFormDto) {
        User savedUser = userService.signUp(Optional.ofNullable(signUpFormDto));
        SavedUserDto savedUserDto = new SavedUserDto(savedUser.getEmail(), savedUser.getNickname());
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    @GetMapping("/verification")
    public void verification(@RequestParam String code) {
        userService.verify(code);
    }
}
