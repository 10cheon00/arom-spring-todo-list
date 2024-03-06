package com.taekcheonkim.todolist.account.service;

import com.taekcheonkim.todolist.account.domain.User;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
import com.taekcheonkim.todolist.account.exception.InvalidUserFormException;
import com.taekcheonkim.todolist.account.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private final UserFormDto userFormDto;
    private final User alreadyExistUser;

    public UserServiceTest() {
        String email = "test@domain.com";
        String password = "testpassword";
        String nickname = "testnickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        this.alreadyExistUser = new User(email, password, nickname);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void successSignUpWithValidUserFormDto() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        // when
        User signInUser = userService.signUp(Optional.of(userFormDto));
        // then
        assertThat(signInUser.getEmail()).isEqualTo(userFormDto.getEmail());
    }

    @Test
    void failSignUpWhenEmailAlreadyExists() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(alreadyExistUser));
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signUp(Optional.of(userFormDto));
        }).isInstanceOf(InvalidUserFormException.class);
    }

    @Test
    void failSignUpWhenInvalidEmail() {
        // given
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(alreadyExistUser));
        // when
        // then
        assertThatThrownBy(() -> {
            userService.signUp(Optional.of(userFormDto));
        }).isInstanceOf(InvalidUserFormException.class);
    }
}
