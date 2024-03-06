package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    protected final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<SavedUserDto> signUp(UserFormDto userFormDto) {
        User savedUser = userService.signUp(Optional.ofNullable(userFormDto));
        SavedUserDto savedUserDto = new SavedUserDto(savedUser.getEmail(), savedUser.getNickname());
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }
}