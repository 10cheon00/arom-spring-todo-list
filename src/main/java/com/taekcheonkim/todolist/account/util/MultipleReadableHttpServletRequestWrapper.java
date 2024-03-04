package com.taekcheonkim.todolist.account.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * <p>
 * This code was written by reffering to
 * <a href="https://stackoverflow.com/questions/10210645/http-servlet-request-lose-params-from-post-body-after-read-it-once/17129256#17129256">
 * Http Servlet request lose params from POST body after read it once | Stack Overflow
 * </a>
 * </p>
 */
public class MultipleReadableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public MultipleReadableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
}
