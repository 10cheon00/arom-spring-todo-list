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

import static org.assertj.core.api.Assertions.assertThat;


public class ContentCachingRequestAndResponseWrapperFilterTest {
    private final ContentCachingRequestAndResponseWrapperFilter wrapperFilter;

    public ContentCachingRequestAndResponseWrapperFilterTest() {
        this.wrapperFilter = new ContentCachingRequestAndResponseWrapperFilter();
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
        class FakeFilterChain extends MockFilterChain {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) {
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
                ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
                assertThat(requestWrapper.getContentAsByteArray()).isNotNull();
                assertThat(requestWrapper.getContentAsByteArray()).isNotNull();
            }
        }
        FakeFilterChain fakeFilterChain = new FakeFilterChain();
        // when
        wrapperFilter.doFilterInternal(request, response, fakeFilterChain);
        // then
    }
}
