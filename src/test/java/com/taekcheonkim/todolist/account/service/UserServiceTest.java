package com.taekcheonkim.todolist.account.service;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidUserFormException;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private final UserService userService;
    @Mock
    private UserRepository userRepository;
    private final UserFormDto userFormDto;
    private final User alreadyExistUser;

    public UserServiceTest() {
        this.userService = new UserService(userRepository);

        String email = "test@domain.com";
        String password = "testpassword";
        String nickname = "testnickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        this.alreadyExistUser = new User(email, password, nickname);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void successSignUpWithValidUserFormDto() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        // when
        User signInUser = userService.signIn(userFormDto);
        // then
        assertThat(signInUser.getEmail()).isEqualTo(userFormDto.getEmail());
    }

    @Test
    void failSignUpWhenEmailAlreadyExists() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(alreadyExistUser);
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signIn(userFormDto);
        }).isInstanceOf(InvalidUserFormException.class);
    }

    @Test
    void failSignUpWhenInvalidEmail() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(alreadyExistUser);
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signIn(userFormDto);
        }).isInstanceOf(InvalidUserFormException.class);
    }

    @Test
    void failSignUpWhenNicknameAlreadyExists() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(alreadyExistUser);
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signIn(userFormDto);
        }).isInstanceOf(InvalidUserFormException.class);
    }
}
