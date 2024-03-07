package com.taekcheonkim.todolist.account.controller;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.dto.SignInFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidLoginFormException;
import com.taekcheonkim.todolist.account.authentication.SessionAttributes;
import com.taekcheonkim.todolist.account.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

// todo: RestController를 UserController에 붙이면 테스트를 수정해야함
@RestController
@ConditionalOnProperty(name = "account.authentication", havingValue = "session")
public class SessionUserController extends UserController {
    public SessionUserController(UserService userService, AuthenticationManager authenticationManager) {
        super(userService, authenticationManager);
    }

    @PostMapping(SignInPath)
    public void signIn(@RequestBody SignInFormDto signInFormDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.ofNullable(signInFormDto));
        if (authenticatedUserHolder.isAuthenticated()) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute(SessionAttributes.Authenticated, true);
            httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, authenticatedUserHolder);
            response.sendRedirect("/");
        } else {
            throw new InvalidLoginFormException();
        }
    }
}
