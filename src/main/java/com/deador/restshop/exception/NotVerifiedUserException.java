package com.deador.restshop.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotVerifiedUserException extends UserPermissionException {
    private static final long serialVersionUID = 1L;
    private static final String NOT_VERIFIED_USER_EXCEPTION = "User is not verified";

    public NotVerifiedUserException() {
        super(NOT_VERIFIED_USER_EXCEPTION);
    }

    public NotVerifiedUserException(String message) {
        super(message.isEmpty() ? NOT_VERIFIED_USER_EXCEPTION : message);
    }
}
