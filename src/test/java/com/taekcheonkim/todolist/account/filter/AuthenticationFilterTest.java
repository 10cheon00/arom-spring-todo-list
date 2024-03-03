package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.SerializationUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <h1>AuthenticationFilter</h1>
 * <p>Request Body에 들어있는 LoginDto를 획득한다.</p>
 * <p>Bean으로 등록된 AuthenticationManager의 authenticate를 호출하고, LoginDto를 인수로 전달한다.</p>
 * <h1>Test Purpose</h1>
 * <p>이 테스트는 ContentCachingRequestWrapper를 Mock 객체로 설정한다. </p>
 * <p>AuthenticationManager의 authenticate를 호출할 때 적절한 LoginDto를 인자로 전달하였는지 검사한다.</p>
 */
public class AuthenticationFilterTest {
    @Mock
    private ContentCachingRequestWrapper requestWrapper;
    @Mock
    private ContentCachingResponseWrapper responseWrapper;
    @Mock
    private FilterChain filterChain;
    @Mock
    private AuthenticationManager authenticationManager;
    private final AuthenticationFilter authenticationFilter;
    private final LoginDto loginDto;

    public AuthenticationFilterTest() {
        this.authenticationFilter = new AuthenticationFilter(authenticationManager);
        String email = "user1@test.com";
        String password = "password";
        this.loginDto = new LoginDto(email, password);
    }

    @BeforeEach
    void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(filterChain).doFilter(requestWrapper, responseWrapper);
    }

    @Test
    void delegateLoginDtoToAuthenticationManager() throws ServletException, IOException {
        // given
        ArgumentCaptor<LoginDto> argumentCaptor = ArgumentCaptor.forClass(LoginDto.class);
        when(requestWrapper.getContentAsByteArray()).thenReturn(SerializationUtils.serialize(loginDto));
        // when
        authenticationFilter.doFilterInternal(requestWrapper, responseWrapper, filterChain);
        // then
        verify(authenticationManager).authenticate(Optional.of(argumentCaptor.capture()));
        assertThat(loginDto).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void delegateEmptyLoginDtoToAuthenticationManagerWhenRequestHasInvalidCredential() throws ServletException, IOException {
        // given
        ArgumentCaptor<LoginDto> argumentCaptor = ArgumentCaptor.forClass(LoginDto.class);
        when(requestWrapper.getContentAsByteArray()).thenReturn(SerializationUtils.serialize(null));
        // when
        authenticationFilter.doFilterInternal(requestWrapper, responseWrapper, filterChain);
        // then
        verify(authenticationManager).authenticate(Optional.of(argumentCaptor.capture()));
        assertThat(argumentCaptor.getValue()).isNull();
    }
}
