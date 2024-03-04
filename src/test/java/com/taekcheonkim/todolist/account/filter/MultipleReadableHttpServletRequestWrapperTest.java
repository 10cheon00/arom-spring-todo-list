package com.taekcheonkim.todolist.account.filter;

import com.taekcheonkim.todolist.account.util.MultipleReadableHttpServletRequestWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MultipleReadableHttpServletRequestWrapperTest {
    @Test
    void readRequestBodyTwiceInWrapper() throws IOException {
        // given
        byte[] content = "1234567890abcdefghijklmnopqrstuvwxyz".getBytes();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(content);
        // when
        MultipleReadableHttpServletRequestWrapper requestWrapper = new MultipleReadableHttpServletRequestWrapper(request);
        // then
        assertThat(requestWrapper.getInputStream().readAllBytes()).isEqualTo(content);
        assertThat(requestWrapper.getInputStream().readAllBytes()).isEqualTo(content);
    }
}
