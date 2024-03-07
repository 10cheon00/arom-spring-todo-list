package com.taekcheonkim.todolist.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.controller.SessionUserController;
import com.taekcheonkim.todolist.account.dto.SignInFormDto;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.SignUpFormDto;
import com.taekcheonkim.todolist.account.exception.UserExceptionHandler;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SessionUserMvcTest {
    private MockMvc mockMvc;
    @Autowired
    private SessionUserController sessionUserController;
    private final SignUpFormDto signUpFormDto;
    private final SavedUserDto savedUserDto;
    private final SignInFormDto signInFormDto;
    private final byte[] userFormDtoBytes;
    private final byte[] savedUserDtoBytes;
    private final byte[] loginDtoBytes;

    public SessionUserMvcTest() throws JsonProcessingException {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.signUpFormDto = new SignUpFormDto(email, password, nickname);
        this.savedUserDto = new SavedUserDto(email, nickname);
        this.signInFormDto = new SignInFormDto(email, password);

        ObjectMapper objectMapper = new ObjectMapper();
        this.userFormDtoBytes = objectMapper.writeValueAsBytes(signUpFormDto);
        this.savedUserDtoBytes = objectMapper.writeValueAsBytes(savedUserDto);
        this.loginDtoBytes = objectMapper.writeValueAsBytes(signInFormDto);
    }

    MockHttpServletRequestBuilder signUpRequest(byte[] content) {
        return MockMvcRequestBuilders
                .post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
    }

    MockHttpServletRequestBuilder signInRequest(byte[] content) {
        return MockMvcRequestBuilders
                .post("/users/signin")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(sessionUserController)
                .setControllerAdvice(new UserExceptionHandler())
                .build();
    }

    @Test
    void successSignUp() throws Exception {
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isOk())
                .andExpect(content().bytes(savedUserDtoBytes));
    }

    @Test
    void failSignUpWithAlreadyExistEmail() throws Exception {
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isCreated());
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isBadRequest());
    }

    @Test
    void failSignUpWithNullUserFormDto() throws Exception {
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isCreated());
        mockMvc.perform(signUpRequest(null))
                .andExpect(status().isBadRequest());
    }

    @Test
    void successSignIn() throws Exception {
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isCreated());
        mockMvc.perform(signInRequest(loginDtoBytes))
                .andExpect(status().isOk());
    }

    @Test
    void failSignInWithInvalidLoginDto() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SignInFormDto invalidSignInFormDto = new SignInFormDto("invalid@email.com", "password");
        byte[] invalidLoginDtoBytes = objectMapper.writeValueAsBytes(invalidSignInFormDto);
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isCreated());
        mockMvc.perform(signInRequest(invalidLoginDtoBytes))
                .andExpect(status().isBadRequest());
    }

    @Test
    void failSignInWithNullLoginDto() throws Exception {
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isCreated());
        mockMvc.perform(signInRequest(null))
                .andExpect(status().isBadRequest());
    }
}
