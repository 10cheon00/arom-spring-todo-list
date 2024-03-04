package com.taekcheonkim.todolist.account.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;

/**
 * <p>
 * This code was written by reffering to
 * <a href="https://stackoverflow.com/questions/10210645/http-servlet-request-lose-params-from-post-body-after-read-it-once/17129256#17129256">
 * Http Servlet request lose params from POST body after read it once | Stack Overflow
 * </a>
 * </p>
 */
public class MultipleReadableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private ByteArrayOutputStream cachedBytes;

    public MultipleReadableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null) {
            cacheInputStream();
        }

        return new CachedServletInputStream(cachedBytes.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
        cachedBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    private static class CachedServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public boolean isFinished() {
            return this.buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public int read() {
            return this.buffer.read();
        }
    }
}
