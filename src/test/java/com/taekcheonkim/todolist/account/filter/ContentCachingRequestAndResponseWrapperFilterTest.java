package com.taekcheonkim.todolist.account.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class ContentCachingRequestAndResponseWrapperFilterTest {
    private final ContentCachingRequestAndResponseWrapperFilter wrapperFilter;
    private final byte[] content;

    public ContentCachingRequestAndResponseWrapperFilterTest() {
        this.wrapperFilter = new ContentCachingRequestAndResponseWrapperFilter();
        this.content = "1234567890".getBytes();
    }

    @Test
    void wrapRequestAndResponseWithFakeFilterChain() throws ServletException, IOException {
        // given
        // when
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        class FakeFilterChain extends MockFilterChain {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                assertThat(request).isInstanceOf(ContentCachingRequestWrapper.class);
                assertThat(response).isInstanceOf(ContentCachingResponseWrapper.class);
            }
        }
        FakeFilterChain fakeFilterChain = new FakeFilterChain();
        wrapperFilter.doFilterInternal(request, response, fakeFilterChain);
        // then
    }

    @Test
    void getContentTwiceFromWrappingRequestInFakeFilterChain() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(content);
        class FakeFilterChain extends MockFilterChain {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException {
                ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
                // 한 번 InputStream으로 읽어야 getContentAs~를 통해 캐싱된 body를 획득 가능하다.
                assertThat(requestWrapper.getInputStream().readAllBytes()).isEqualTo(content);
                assertThat(requestWrapper.getContentAsByteArray()).isEqualTo(content);
            }
        }
        FakeFilterChain fakeFilterChain = new FakeFilterChain();
        // when
        wrapperFilter.doFilterInternal(request, response, fakeFilterChain);
        // then
    }
}
