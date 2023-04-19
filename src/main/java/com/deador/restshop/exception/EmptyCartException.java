package com.deador.restshop.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyCartException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String EMPTY_CART_EXCEPTION = "Empty cart";

    public EmptyCartException() {
        super(EMPTY_CART_EXCEPTION);
    }

    public EmptyCartException(String message) {
        super(message.isEmpty() ? EMPTY_CART_EXCEPTION : message);
    }
}
