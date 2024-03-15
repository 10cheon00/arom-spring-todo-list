package com.taekcheonkim.todolist.user.config;

import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.filter.AuthenticationFilter;
import com.taekcheonkim.todolist.user.filter.JwtAuthenticationFilter;
import com.taekcheonkim.todolist.user.filter.SessionAuthenticationFilter;
import com.taekcheonkim.todolist.user.repository.UserRepository;
import com.taekcheonkim.todolist.user.util.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "user.authentication.method", havingValue = "jwt")
@Configuration
public class JwtAuthenticationConfig implements AuthenticationConfig {
    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public JwtAuthenticationConfig(AuthenticationContext authenticationContext, UserRepository userRepository, JwtGenerator jwtGenerator) {
        this.authenticationContext = authenticationContext;
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean(FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean) {
        filterRegistrationBean.setFilter(jwtAuthenticationFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authenticationContext, userRepository, jwtGenerator);
    }
}
