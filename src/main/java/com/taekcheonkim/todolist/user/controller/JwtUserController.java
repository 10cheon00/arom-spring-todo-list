package com.taekcheonkim.todolist.user.controller;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignInFormDto;
import com.taekcheonkim.todolist.user.exception.InvalidSignInFormException;
import com.taekcheonkim.todolist.user.service.UserService;
import com.taekcheonkim.todolist.user.util.JwtGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

@RestController
@ConditionalOnProperty(name = "user.authentication.method", havingValue = "jwt")
public class JwtUserController extends UserController {
    public final JwtGenerator jwtGenerator;

    public JwtUserController(UserService userService, AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        super(userService, authenticationManager);
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping(SignInPath)
    @ResponseStatus(HttpStatus.OK)
    public void signIn(@RequestBody SignInFormDto signInFormDto, HttpServletRequest request, HttpServletResponse response) throws InvalidSignInFormException, IOException {
        if (signInFormDto == null) {
            throw new InvalidSignInFormException("SignIn is Failed due to invalid form.");
        }
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.ofNullable(signInFormDto));
        if (authenticatedUserHolder.isAuthenticated()) {
            // generate jwt token.
            User user = authenticatedUserHolder.getAuthenticatedUser().get();
            String token = jwtGenerator.generateToken(user);

            // return jwt token.
            Writer bodyWriter = response.getWriter();
            bodyWriter.write(token);
        } else {
            throw new InvalidSignInFormException("SignIn is Failed due to invalid form.");
        }
    }
}
