package com.taekcheonkim.todolist.user.authorization;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h1>AuthorizationInterceptor</h1>
 * <p>Dispatch가 일어난 후 실행될 컨트롤러의 메서드 또는 컨트롤러에 @PreAuthorize가 있는지 확인한다.</p>
 * <p>만약 @PreAuthorize가 있다면 AuthenticationContext를 조회하여 authenticated되었는지 확인한다.</p>
 * <p>만약 authenticated라면 true를, 그렇지 않다면 false를 반환한다.</p>
 * <h1>Test Purpose</h1>
 * <ul>
 *     <li>@PreAuthorize가 없다면, true를 반환하는지 확인한다.</li>
 *     <li>@PreAuthorize가 있다면, authenticated일 때 true를 반환하는지 확인한다.</li>
 *     <li>@PreAuthorize가 있다면, authenticated일 때 false를 반환하는지 확인한다.</li>
 * </ul>
 */
public class AuthorizationInterceptorTest {
    private final AuthorizationInterceptor authorizationInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    private final AuthenticationContext authenticationContext;
    private final AuthenticatedUserHolder authenticatedUserHolder;
    private final AuthenticatedUserHolder notAuthenticatedUserHolder;

    private HandlerMethod handlerMethod;
    private Method method;
    private final PreAuthorizeInMethodController preAuthorizeInMethodController;
    private final PreAuthorizeInClassController preAuthorizeInClassController;
    private final NoAuthorizeController noAuthorizeController;

    public AuthorizationInterceptorTest() {
        User user = new User("email", "password", "");
        this.authenticatedUserHolder = new AuthenticatedUserHolder(Optional.of(user));
        this.notAuthenticatedUserHolder = new AuthenticatedUserHolder(Optional.empty());
        this.authenticationContext = new AuthenticationContext();
        this.preAuthorizeInClassController = new PreAuthorizeInClassController();
        this.preAuthorizeInMethodController = new PreAuthorizeInMethodController();
        this.noAuthorizeController = new NoAuthorizeController();
        this.authorizationInterceptor = new AuthorizationInterceptor(authenticationContext);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void noAuthorizeWhenRequestUrlPatternNotRequireAuthorization() throws Exception {
        // given
        authenticationContext.setAuthenticatedUserHolder(notAuthenticatedUserHolder);
        method = NoAuthorizeController.class.getMethod("foo");
        handlerMethod = new HandlerMethod(noAuthorizeController, method);
        // when
        boolean state = authorizationInterceptor.preHandle(request, response, handlerMethod);
        // then
        assertThat(state).isEqualTo(true);
    }

    @Test
    void successAuthorizationWithPreAuthorizeInMethodController() throws Exception {
        // given
        authenticationContext.setAuthenticatedUserHolder(authenticatedUserHolder);
        method = PreAuthorizeInMethodController.class.getMethod("foo");
        handlerMethod = new HandlerMethod(preAuthorizeInMethodController, method);
        // when
        boolean state = authorizationInterceptor.preHandle(request, response, handlerMethod);
        // then
        assertThat(state).isEqualTo(true);
    }

    @Test
    void successAuthorizationWithPreAuthorizeInClassController() throws Exception {
        // given
        authenticationContext.setAuthenticatedUserHolder(authenticatedUserHolder);
        method = PreAuthorizeInClassController.class.getMethod("foo");
        handlerMethod = new HandlerMethod(preAuthorizeInClassController, method);
        // when
        boolean state = authorizationInterceptor.preHandle(request, response, handlerMethod);
        // then
        assertThat(state).isEqualTo(true);
    }

    @Test
    void failAuthorizationWithPreAuthorizeInClassControllerWhenNotAuthenticated() throws Exception {
        // given
        authenticationContext.setAuthenticatedUserHolder(notAuthenticatedUserHolder);
        method = PreAuthorizeInClassController.class.getMethod("foo");
        handlerMethod = new HandlerMethod(preAuthorizeInClassController, method);
        // when
        boolean state = authorizationInterceptor.preHandle(request, response, handlerMethod);
        // then
        assertThat(state).isEqualTo(false);
    }

    @Test
    void failAuthorizationWithPreAuthorizeInMethodControllerWhenNotAuthenticated() throws Exception {
        // given
        authenticationContext.setAuthenticatedUserHolder(notAuthenticatedUserHolder);
        method = PreAuthorizeInMethodController.class.getMethod("foo");
        handlerMethod = new HandlerMethod(preAuthorizeInMethodController, method);
        // when
        boolean state = authorizationInterceptor.preHandle(request, response, handlerMethod);
        // then
        assertThat(state).isEqualTo(false);
    }

    @RestController
    public static class PreAuthorizeInMethodController {
        @GetMapping("")
        @PreAuthorize
        public String foo() {
            return "bar";
        }
    }

    @RestController
    @PreAuthorize
    public static class PreAuthorizeInClassController {
        @GetMapping("")
        public String foo() {
            return "bar";
        }
    }

    @RestController
    public static class NoAuthorizeController {
        @GetMapping("")
        public String foo() {
            return "bar";
        }
    }
}
