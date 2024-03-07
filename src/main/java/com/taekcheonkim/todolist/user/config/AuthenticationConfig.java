package com.taekcheonkim.todolist.user.config;

import com.taekcheonkim.todolist.user.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public interface AuthenticationConfig {
    @Bean
    FilterRegistrationBean<AuthenticationFilter> sessionFilterRegistrationBean(FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean);
}