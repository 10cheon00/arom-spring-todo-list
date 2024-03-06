package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public ResponseEntity<SavedUserDto> signUp(UserFormDto userFormDto) {
        User savedUser = userService.signUp(Optional.ofNullable(userFormDto));
        SavedUserDto savedUserDto = new SavedUserDto(savedUser.getEmail(), savedUser.getNickname());
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }
}
