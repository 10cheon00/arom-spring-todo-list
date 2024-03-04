package com.taekcheonkim.todolist.account.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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
    @Mock
    private ContentCachingRequestWrapper requestWrapper;
    @Mock
    private ContentCachingResponseWrapper responseWrapper;
    @Mock
    private FilterChain filterChain;
    private MockHttpSession mockHttpSession;
    private final String authenticationAttribute;
    @Mock
    private AuthenticationManager authenticationManager;
    private SessionAuthenticationFilter sessionAuthenticationFilter;
    private final LoginDto loginDto;
    private final User user;
    private final ObjectMapper objectMapper;
    private final byte[] loginDtoBytes;

    public SessionAuthenticationFilterTest() {
        String email = "user1@test.com";
        String password = "password";
        this.loginDto = new LoginDto(email, password);
        this.user = new User(email, password, "");

        this.authenticationAttribute = "authentication";
        try {
            this.objectMapper = new ObjectMapper();
            this.loginDtoBytes = objectMapper.writeValueAsBytes(loginDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(filterChain).doFilter(requestWrapper, responseWrapper);
        mockHttpSession = new MockHttpSession();
        when(requestWrapper.getSession()).thenReturn(mockHttpSession);
        sessionAuthenticationFilter = new SessionAuthenticationFilter(authenticationManager);
    }

    @Test
    void extractLoginDtoFromContentCachingRequestWrapper() throws ServletException, IOException {
        ArgumentCaptor<Optional<LoginDto>> argumentCaptor = ArgumentCaptor.forClass(Optional.class);
        // given
        when(requestWrapper.getContentAsByteArray()).thenReturn(loginDtoBytes);
        when(authenticationManager.authenticate(any(Optional.class))).thenReturn(new AuthenticatedUserHolder(Optional.of(user)));
        // when
        sessionAuthenticationFilter.doFilterInternal(requestWrapper, responseWrapper, filterChain);
        // then
        verify(authenticationManager).authenticate(argumentCaptor.capture());
        Optional<LoginDto> maybeLoginDto = argumentCaptor.getValue();
        assertThat(maybeLoginDto.isPresent()).isTrue();
        assertThat(maybeLoginDto.get()).isEqualTo(loginDto);
    }

    @Test
    void setSessionAttributeToTrueAfterAuthenticationWithValidLoginDto() throws ServletException, IOException {
        // given
        when(requestWrapper.getContentAsByteArray()).thenReturn(loginDtoBytes);
        when(authenticationManager.authenticate(Optional.of(loginDto))).thenReturn(new AuthenticatedUserHolder(Optional.of(user)));
        // when
        sessionAuthenticationFilter.doFilterInternal(requestWrapper, responseWrapper, filterChain);
        // then
        assertThat(mockHttpSession.getAttribute(authenticationAttribute)).isEqualTo(true);
    }

    @Test
    void setSessionAttributeToFalseAfterAuthenticationWithValidLoginDto() throws ServletException, IOException {
        // given
        when(requestWrapper.getContentAsByteArray()).thenReturn(loginDtoBytes);
        when(authenticationManager.authenticate(Optional.of(loginDto))).thenReturn(new AuthenticatedUserHolder(Optional.empty()));
        // when
        sessionAuthenticationFilter.doFilterInternal(requestWrapper, responseWrapper, filterChain);
        // then
        assertThat(mockHttpSession.getAttribute(authenticationAttribute)).isEqualTo(false);
    }
}
