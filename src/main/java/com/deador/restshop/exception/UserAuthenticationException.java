package com.deador.restshop.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationException extends AuthenticationException {
    private static final String WRONG_AUTHENTICATION_EXCEPTION = "You are not authenticated";

    public UserAuthenticationException() {
        super(WRONG_AUTHENTICATION_EXCEPTION);
    }

    public UserAuthenticationException(String message) {
        super(message.isEmpty() ? WRONG_AUTHENTICATION_EXCEPTION : message);
    }
}
