package com.taekcheonkim.todolist.account.config;

import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.account.filter.AuthenticationFilter;
import com.taekcheonkim.todolist.account.filter.SessionAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "account.authentication", havingValue = "session")
@Configuration
public class SessionAuthenticationConfig implements AuthenticationConfig {
    private final AuthenticationContext authenticationContext;

    @Autowired
    public SessionAuthenticationConfig(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    public FilterRegistrationBean<AuthenticationFilter> sessionFilterRegistrationBean(FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean) {
        filterRegistrationBean.setFilter(sessionAuthenticationFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter(authenticationContext);
    }
}