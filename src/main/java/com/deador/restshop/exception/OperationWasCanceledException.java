package com.deador.restshop.exception;

public class OperationWasCanceledException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String OPERATION_WAS_CANCELED_EXCEPTION = "Operation was canceled";

    public OperationWasCanceledException() {
        super(OPERATION_WAS_CANCELED_EXCEPTION);
    }

    public OperationWasCanceledException(String message) {
        super(message.isEmpty() ? OPERATION_WAS_CANCELED_EXCEPTION : message);
    }
}
