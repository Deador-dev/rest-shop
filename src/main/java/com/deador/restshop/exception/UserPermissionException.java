package com.deador.restshop.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserPermissionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String USER_PERMISSION_EXCEPTION = "You have no necessary permissions";

    public UserPermissionException() {
        super(USER_PERMISSION_EXCEPTION);
    }

    public UserPermissionException(String message) {
        super(message.isEmpty() ? USER_PERMISSION_EXCEPTION : message);
    }
}
