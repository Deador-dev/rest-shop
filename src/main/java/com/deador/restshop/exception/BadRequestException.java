package com.deador.restshop.exception;

import org.springframework.lang.Nullable;

public class BadRequestException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
    private static final String BAD_REQUEST_EXCEPTION = "Bad request";

    public BadRequestException() {
        super(BAD_REQUEST_EXCEPTION);
    }

    public BadRequestException(@Nullable String message) {
        super((message == null || message.isEmpty()) ? BAD_REQUEST_EXCEPTION : message);
    }
}
