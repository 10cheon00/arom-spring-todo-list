package com.taekcheonkim.todolist.account.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationContext;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <h1>SessionAuthenticationFilter</h1>
 * <p>Request의 Body에서 LoginDto를 획득한다.</p>
 * <p>획득한 LoginDto를 AuthenticationManager에게 넘겨주어 AuthenticatedUserHolder를 획득한다.</p>
 * <p>AuthenticatedUserHolder에 User가 담겨있으면 인증된 것이므로 세션에 인증 정보를 추가한다.</p>
 * <p>만약 LoginDto를 획득하였지만 AuthenticatedUserHolder에 User가 포함되어 있지 않으면, 인증에 실패한 것이므로 세션에서 인증 정보를 삭제한다.</p>
 * <h1>Test Purpose</h1>
 * <ul>
 *   <li>RequestBody로부터 LoginDto를 획득하였는지, 그리고 AuthenticationManager에게 전달하였는지 검사한다.</li>
 *   <li>AuthenticatedUserHolder에 User가 담겨있을 때 세션이 갱신되는지 검사한다.</li>
 *   <li>AuthenticatedUserHolder에 User가 담겨있지 않을 때 세션이 갱신되는지 검사한다.</li>
 * </ul>
 */
public class SessionAuthenticationFilterTest {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private MockHttpSession httpSession;
    private String attributeKeyOfAuthenticated;
    private String attributeKeyOfAuthenticatedUserHolder;

    @Mock
    private AuthenticationManager authenticationManager;
    private AuthenticationContext authenticationContext;
    private final AuthenticatedUserHolder authenticatedUserHolder;
    private final AuthenticatedUserHolder notAuthenticatedUserHolder;
    private SessionAuthenticationFilter sessionAuthenticationFilter;
    private final LoginDto loginDto;
    private final User user;
    private final byte[] loginDtoBytes;

    public SessionAuthenticationFilterTest() {
        String email = "user1@test.com";
        String password = "password";
        this.loginDto = new LoginDto(email, password);
        this.user = new User(email, password, "");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.loginDtoBytes = objectMapper.writeValueAsBytes(loginDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.authenticatedUserHolder = new AuthenticatedUserHolder(Optional.of(this.user));
        this.notAuthenticatedUserHolder = new AuthenticatedUserHolder(Optional.empty());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationContext = new AuthenticationContext();
        sessionAuthenticationFilter = new SessionAuthenticationFilter(authenticationManager, authenticationContext);
        request = new MockHttpServletRequest();
        httpSession = new MockHttpSession();
        request.setSession(httpSession);
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        attributeKeyOfAuthenticated = sessionAuthenticationFilter.getAttributeKeyOfAuthenticate();
        attributeKeyOfAuthenticatedUserHolder = sessionAuthenticationFilter.getAttributeKeyOfAuthenticatedUserHolder();
    }

    @Test
    void extractLoginDtoFromContentCachingRequestWrapper() throws ServletException, IOException {
        ArgumentCaptor<Optional<LoginDto>> argumentCaptor = ArgumentCaptor.forClass(Optional.class);
        // given
        request.setContent(loginDtoBytes);
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(authenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        verify(authenticationManager).authenticate(argumentCaptor.capture());
        Optional<LoginDto> maybeLoginDto = argumentCaptor.getValue();
        assertThat(maybeLoginDto.isPresent()).isTrue();
        assertThat(maybeLoginDto.get()).isEqualTo(loginDto);
    }

    @Test
    void successAuthenticationWithValidLoginDto() throws ServletException, IOException {
        // given
        request.setContent(loginDtoBytes);
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(authenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        assertThat(httpSession.getAttribute(attributeKeyOfAuthenticated)).isEqualTo(true);
        assertThat(authenticationContext.getAuthenticatedUserHolder().isAuthenticated()).isEqualTo(true);
    }

    @Test
    void failAuthenticationWithNotFoundUser() throws ServletException, IOException {
        // given
        request.setContent(loginDtoBytes);
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        assertThat(httpSession.getAttribute(attributeKeyOfAuthenticated)).isEqualTo(false);
        assertThat(authenticationContext.getAuthenticatedUserHolder().isAuthenticated()).isEqualTo(false);
    }

    @Test
    void successAuthenticationWithNoLoginDtoButSessionAttributeIsTrue() throws ServletException, IOException {
        // given
        request.setContent(null);
        httpSession.setAttribute(attributeKeyOfAuthenticated, true);
        httpSession.setAttribute(attributeKeyOfAuthenticatedUserHolder, authenticatedUserHolder);
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(notAuthenticatedUserHolder);
        // when
        sessionAuthenticationFilter.doFilterInternal(request, response, filterChain);
        // then
        assertThat(httpSession.getAttribute(attributeKeyOfAuthenticated)).isEqualTo(true);
        assertThat(authenticationContext.getAuthenticatedUserHolder().isAuthenticated()).isEqualTo(true);
    }
}
