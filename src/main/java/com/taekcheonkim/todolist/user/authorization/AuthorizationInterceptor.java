package com.taekcheonkim.todolist.user.authorization;

import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@RequestScope
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final AuthenticationContext authenticationContext;
    private boolean hasPreAuthorizeAnnotation;

    public AuthorizationInterceptor(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        findPreAuthorizeAnnotationInHandler(handler);
        if (hasPreAuthorizeAnnotation) {
            return authenticationContext.getAuthenticatedUserHolder().isAuthenticated();
        }
        return true;
    }

    private void findPreAuthorizeAnnotationInHandler(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> controller = handlerMethod.getBean().getClass();
        Method method = handlerMethod.getMethod();
        this.hasPreAuthorizeAnnotation = controller.isAnnotationPresent(PreAuthorize.class) || method.isAnnotationPresent(PreAuthorize.class);
    }
}
