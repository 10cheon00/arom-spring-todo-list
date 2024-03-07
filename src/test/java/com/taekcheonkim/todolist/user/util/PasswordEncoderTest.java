package com.taekcheonkim.todolist.user.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void CheckPasswordCrypted() {
        // given
        String secretKey = "testSecretKey";
        PasswordEncoder passwordEncoder = new PasswordEncoder(secretKey);
        // when
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        // then
        assertThat(encodedPassword.length()).isEqualTo(64);
    }
}
