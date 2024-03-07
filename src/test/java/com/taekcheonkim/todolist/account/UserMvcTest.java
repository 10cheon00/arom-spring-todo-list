package com.taekcheonkim.todolist.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taekcheonkim.todolist.account.controller.SessionUserController;
import com.taekcheonkim.todolist.account.dto.SavedUserDto;
import com.taekcheonkim.todolist.account.dto.UserFormDto;
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
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserMvcTest {
    private MockMvc mockMvc;
    @Autowired
    private SessionUserController sessionUserController;
    private final UserFormDto userFormDto;
    private final byte[] userFormDtoBytes;
    private final SavedUserDto savedUserDto;
    private final byte[] savedUserDtoBytes;


    public UserMvcTest() throws JsonProcessingException {
        String email = "email@test.com";
        String password = "password";
        String nickname = "nickname";
        this.userFormDto = new UserFormDto(email, password, nickname);
        ObjectMapper objectMapper = new ObjectMapper();
        this.userFormDtoBytes = objectMapper.writeValueAsBytes(userFormDto);
        this.savedUserDto = new SavedUserDto(email, nickname);
        this.savedUserDtoBytes = objectMapper.writeValueAsBytes(savedUserDto);
    }

    MockHttpServletRequestBuilder signUpRequest(byte[] content) {
        return MockMvcRequestBuilders
                .post("/users/signup")
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
                .andExpect(status().isCreated())
                .andExpect(content().bytes(savedUserDtoBytes));
    }

    @Test
    void failSignUpWithAlreadyExistEmail() throws Exception {
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isCreated());
        mockMvc.perform(signUpRequest(userFormDtoBytes))
                .andExpect(status().isBadRequest());
    }
}
