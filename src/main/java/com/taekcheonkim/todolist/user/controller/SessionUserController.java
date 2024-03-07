package com.taekcheonkim.todolist.user.controller;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.user.dto.SignInFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignUpFormException;
import com.taekcheonkim.todolist.user.authentication.SessionAttributes;
import com.taekcheonkim.todolist.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

// todo: RestController를 UserController에 붙이면 테스트를 수정해야함
@RestController
@ConditionalOnProperty(name = "user.authentication", havingValue = "session")
public class SessionUserController extends UserController {
    public SessionUserController(UserService userService, AuthenticationManager authenticationManager) {
        super(userService, authenticationManager);
    }

    @PostMapping(SignInPath)
    @ResponseStatus(HttpStatus.OK)
    public void signIn(@RequestBody SignInFormDto signInFormDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.ofNullable(signInFormDto));
        if (authenticatedUserHolder.isAuthenticated()) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(SessionAttributes.Authenticated, true);
            httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, authenticatedUserHolder);
        } else {
            throw new InvalidSignUpFormException();
        }
    }
}
