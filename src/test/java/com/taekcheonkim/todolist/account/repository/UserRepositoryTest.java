package com.taekcheonkim.todolist.account.repository;

import com.taekcheonkim.todolist.account.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    private final String email = "user1@test.com";
    private final String password = "password123";
    private final String nickname = "test-user";

    private final UserRepository userRepository;

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
    public void ReadAllUser() {
        // given
        User otherUser = new User("other-user@test.com", "password", "other-user");
        // when
        userRepository.save(this.generateUser());
        userRepository.save(otherUser);
        List<User> result = userRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(2);
    }
}
