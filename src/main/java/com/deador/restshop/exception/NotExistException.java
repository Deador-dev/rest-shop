package com.deador.restshop.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotExistException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private static final String NOT_EXIST_EXCEPTION = "Not exist";

    public NotExistException() {
        super(NOT_EXIST_EXCEPTION);
    }

    public NotExistException(String message) {
        super(message.isEmpty() ? NOT_EXIST_EXCEPTION : message);
    }
}
