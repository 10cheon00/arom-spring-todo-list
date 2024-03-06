package com.taekcheonkim.todolist.account.config;

import com.taekcheonkim.todolist.account.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public interface AuthenticationConfig {
    @Bean
    FilterRegistrationBean<AuthenticationFilter> sessionFilterRegistrationBean(FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean);
}