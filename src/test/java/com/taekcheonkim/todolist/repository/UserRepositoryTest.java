package com.taekcheonkim.todolist.repository;

import com.taekcheonkim.todolist.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    private final UserRepository userRepository;
    private final String email = "user1@test.com";
    private final String password = "password123";
    private final String nickname = "test-user";

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User generateUser() {
        return new User("user1@test.com", "password123", "test-user");
    }

    @Test
    public void CreateUser() {
        // given
        // when
        userRepository.save(this.generateUser());
        // then
        List<User> result = userRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void CreateUserWithDuplicatedEmail() {
        // given
        userRepository.save(this.generateUser());
        // when
        User duplicatedEmailUser = new User(email, password, nickname);
        assertThatException().isThrownBy(() -> {
            userRepository.save(duplicatedEmailUser);
        }).isEqualTo(DuplicatedUserEmailException.class);
        // then
        List<User> result = userRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void CreateUserWithDuplicatedNickname() {
        // given
        userRepository.save(this.generateUser());
        // when
        User duplicatedEmailUser = new User("anotheruser@test.com", password, nickname);
        assertThatException().isThrownBy(() -> {
            userRepository.save(duplicatedEmailUser);
        }).isEqualTo(DuplicatedUserNicknameException.class);
        // then
        List<User> result = userRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void ReadUserByEmail() {
        // given
        User user = this.generateUser();
        userRepository.save(user);
        // when
        User foundUser = userRepository.findByEmail(email);
        // then
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void CheckPasswordCrypted() {
        // given
        User user = this.generateUser();
        userRepository.save(user);
        // when
        User foundUser = userRepository.findByEmail(user.getEmail());
        // then
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        assertThat(foundUser.getPassword()).isEqualTo(passwordEncoder.encode(password));
    }
}
