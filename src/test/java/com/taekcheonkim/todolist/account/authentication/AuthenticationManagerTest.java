package com.taekcheonkim.todolist.account.authentication;

import com.taekcheonkim.todolist.account.authentication.AuthenticatedUserHolder;
import com.taekcheonkim.todolist.account.authentication.AuthenticationManager;
import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.LoginDto;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * <h1>AuthenticationManager</h1>
 * <p>LoginDto가 유효한지 검사하여 AuthenticatedUserHolder를 반환한다.</p>
 * <p>PasswordEncoder를 사용하여 LoginDto의 password를 encode한 값과,
 * LoginDto의 email를 이용해 조회한 User의 password가 동일할 경우 AuthenticatedUserHolder에 User를,
 * 그렇지 않은 경우 Optional.empty()를 담는다.
 * </p>
 * <h1>Test Purpose</h1>
 * <ul>
 *  <li>유효한 LoginDto가 담겨져 있지 않을 경우 Optional.empty()가 담긴 AuthenticatedUserHolder가 반환되는가를 확인한다.</li>
 *  <li>유효한 LoginDto가 담겨져 있을 때 email을 이용해 UserRepository를 조회하여 User를 획득,
 *      획득한 User의 password와 LoginDto의 password를 encode한 값이 동일할 때
 *      User가 담긴 AuthenticatedUserHolder가 반환되는가를 확인한다..
 *  </li>
 * </ul>
 */
public class AuthenticationManagerTest {
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    private LoginDto loginDto;
    private final User user;
    private final String email;
    private final String password;

    public AuthenticationManagerTest() {
        email = "user1@test.com";
        password = "password";
        user = new User(email, password, "");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationManager = new AuthenticationManager(userRepository);
        loginDto = new LoginDto(email, password);
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void retrieveNotEmptyAuthenticatedUserHolderIfAuthenticateWithLoginDto() {
        // given
        // when
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.of(loginDto));
        // then
        assertThat(authenticatedUserHolder.getAuthenticatedUser()).isNotEqualTo(Optional.empty());
    }

    @Test
    void retrieveEmptyAuthenticatedUserHolderIfAuthenticateWithEmptyLoginDto() {
        // given
        // when
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.empty());
        // then
        assertThat(authenticatedUserHolder.getAuthenticatedUser()).isEqualTo(Optional.empty());
    }
}
