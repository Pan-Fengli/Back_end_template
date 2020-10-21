package com.example.demo.exception;

import org.springframework.security.core.AuthenticationException;

public class MyUsernameNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public MyUsernameNotFoundException(String msg) {
        super(msg);
    }

    public MyUsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
