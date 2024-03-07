package com.taekcheonkim.todolist.user.authentication;

import com.taekcheonkim.todolist.user.domain.User;
import com.taekcheonkim.todolist.user.dto.SignInFormDto;
import com.taekcheonkim.todolist.user.repository.UserRepository;
import com.taekcheonkim.todolist.user.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * <h1>AuthenticationManager</h1>
 * <p>SignUpFormDto가 유효한지 검사하여 AuthenticatedUserHolder를 반환한다.</p>
 * <p>PasswordEncoder를 사용하여 SignUpFormDto의 password를 encode한 값과,
 * SignUpFormDto의 email를 이용해 조회한 User의 password가 동일할 경우 AuthenticatedUserHolder에 User를,
 * 그렇지 않은 경우 Optional.empty()를 담는다.
 * </p>
 * <h1>Test Purpose</h1>
 * <ul>
 *  <li>유효한 SignUpFormDto가 담겨져 있지 않을 경우 Optional.empty()가 담긴 AuthenticatedUserHolder가 반환되는가를 확인한다.</li>
 *  <li>유효한 SignUpFormDto가 담겨져 있을 때 email을 이용해 UserRepository를 조회하여 User를 획득,
 *      획득한 User의 password와 SignUpFormDto의 password를 encode한 값이 동일할 때
 *      User가 담긴 AuthenticatedUserHolder가 반환되는가를 확인한다..
 *  </li>
 * </ul>
 */
public class AuthenticationManagerTest {
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    private final SignInFormDto signInFormDto;
    private final User savedUser;
    private final String testSecretKey;

    public AuthenticationManagerTest() {
        String email = "user1@test.com";
        String password = "password";
        this.signInFormDto = new SignInFormDto(email, password);

        // this password is pre-encoded by external site.
        String encodedPassword = "735d44e02bd777f5e6f8e8bd0efc7a4b0f874e62c3c84733d6d929164305fe49";
        this.savedUser = new User(email, encodedPassword, "");
        this.testSecretKey = "testSecretKey";
    }

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class, withSettings().useConstructor(testSecretKey));
        authenticationManager = new AuthenticationManager(userRepository, passwordEncoder);
    }

    @Test
    void retrieveNotEmptyAuthenticatedUserHolderIfAuthenticateWithSignUpFormDto() {
        // given
        when(userRepository.isExistByEmail(any(String.class))).thenReturn(true);
        when(userRepository.findByEmail(any(String.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(any(String.class))).thenCallRealMethod();
        // when
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.of(signInFormDto));
        // then
        assertThat(authenticatedUserHolder.getAuthenticatedUser().isEmpty()).isFalse();
        verify(passwordEncoder, atLeastOnce()).encode(any(String.class));
    }

    @Test
    void retrieveEmptyAuthenticatedUserHolderIfAuthenticateWithEmptySignUpFormDto() {
        // given
        // when
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.empty());
        // then
        assertThat(authenticatedUserHolder.getAuthenticatedUser()).isEqualTo(Optional.empty());
    }

    @Test
    void retrieveEmptyAuthenticatedUserHolderIfAuthenticateWithInvalidSignUpFormDto() {
        // given
        when(userRepository.isExistByEmail(any(String.class))).thenReturn(false);
        // when
        AuthenticatedUserHolder authenticatedUserHolder = authenticationManager.authenticate(Optional.of(signInFormDto));
        // then
        assertThat(authenticatedUserHolder.getAuthenticatedUser()).isEqualTo(Optional.empty());

    }
}
