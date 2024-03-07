package com.taekcheonkim.todolist.user.filter;

import com.taekcheonkim.todolist.user.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.user.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.user.authentication.SessionAttributes;
import com.taekcheonkim.todolist.user.domain.User;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * <h1>SessionAuthenticationFilter</h1>
 * <p>이전에 SignUp을 통해서 로그인을 했다면 Session에 정보가 남아있다.</p>
 * <p>이 필터를 거칠 때마다 Session에 남아있는 정보를 AuthenticationContext에 담는다.</p>
 * <p>만약 로그아웃을 했다면 세션에 정보가 없기 때문에 빈 AuthenticationConext가 된다.</p>
 * <h1>Test Purpose</h1>
 * <ul>
 *   <li>Session이 비어있다면 AuthenticationContext에 빈 값이 담기는지 검사한다.</li>
 *   <li>Session이 차있다면 AuthenticationContext에 User가 담기는지 검사한다.</li>
 * </ul>
 */
public class SessionAuthenticationFilterTest {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private MockHttpSession httpSession;

    private AuthenticationContext authenticationContext;
    private final AuthenticatedUserHolder authenticatedUserHolder;
    private final AuthenticatedUserHolder notAuthenticatedUserHolder;
    private SessionAuthenticationFilter sessionAuthenticationFilter;

    public SessionAuthenticationFilterTest() {
        String email = "user1@test.com";
        String password = "password";
        User user = new User(email, password, "");

        this.authenticatedUserHolder = new AuthenticatedUserHolder(Optional.of(user));
        this.notAuthenticatedUserHolder = new AuthenticatedUserHolder(Optional.empty());
    }

    @BeforeEach
    void setUp() {
        authenticationContext = new AuthenticationContext();
        sessionAuthenticationFilter = new SessionAuthenticationFilter(authenticationContext);
        request = new MockHttpServletRequest();
        httpSession = new MockHttpSession();
        request.setSession(httpSession);
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void successAuthentication() throws ServletException, IOException {
        // given
        httpSession.setAttribute(SessionAttributes.Authenticated, true);
        httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, authenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        assertThat(authenticationContext.getAuthenticatedUserHolder().isAuthenticated()).isEqualTo(true);
    }

    @Test
    void failAuthenticationWhenAuthenticatedAttributeIsNull() throws ServletException, IOException {
        // given
        httpSession.setAttribute(SessionAttributes.Authenticated, null);
        httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, notAuthenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        assertThat(authenticationContext.getAuthenticatedUserHolder().isAuthenticated()).isEqualTo(false);
    }

    @Test
    void failAuthenticationWhenAuthenticatedAttributeIsFalse() throws ServletException, IOException {
        // given
        httpSession.setAttribute(SessionAttributes.Authenticated, false);
        httpSession.setAttribute(SessionAttributes.AuthenticatedUserHolder, notAuthenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        assertThat(authenticationContext.getAuthenticatedUserHolder().isAuthenticated()).isEqualTo(false);
    }
}
