package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public ResponseEntity<SavedUserDto> signUp(UserFormDto userformDto) {
        return null;
    }
    /*
    * SessionAuthentication
    * authenticationContext가 비어있다면 인증 실패
    * 로그인 시
    * 로그아웃 시 세션 초기화
    *
    * JwtAuthentication
    * authenticationContext가 비어있다면 인증 실패
    * jwt토큰 반환
    *
    *
    * */
}
