package com.taekcheonkim.todolist.user.filter;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.repository.UserRepository;
import com.taekcheonkim.todolist.user.util.JwtGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class JwtAuthenticationFilter extends AuthenticationFilter {
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    public JwtAuthenticationFilter(AuthenticationContext authenticationContext, UserRepository userRepository, JwtGenerator jwtGenerator) {
        super(authenticationContext);
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    protected void updateAuthenticationContext(HttpServletRequest request) {
        // check jwt token is valid
        // if valid, find user and set userholder into authentication Context
        String token = getJwtFromRequest(request);

        if (StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
            String email = jwtGenerator.getEmailFromToken(token);
            if (userRepository.isExistByEmail(email)) {
                User user = userRepository.findByEmail(email);
                AuthenticatedUserHolder authenticatedUserHolder = new AuthenticatedUserHolder(Optional.of(user));
                authenticationContext.setAuthenticatedUserHolder(authenticatedUserHolder);
                return;
            }
        }

        AuthenticatedUserHolder notAuthenticatedUserHolder = new AuthenticatedUserHolder(Optional.empty());
        authenticationContext.setAuthenticatedUserHolder(notAuthenticatedUserHolder);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
