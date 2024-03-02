package com.taekcheonkim.todolist.account.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {
    @Test
    public void CheckPasswordCrypted() {
        // given
        String secretKey = "testSecretKey";
        String password = "testPassword";
        PasswordEncoder passwordEncoder = new PasswordEncoder(secretKey);
        // when
        String encodedPassword = passwordEncoder.encode(password);
        // then
        assertThat(encodedPassword.length()).isEqualTo(64);
    }
}
