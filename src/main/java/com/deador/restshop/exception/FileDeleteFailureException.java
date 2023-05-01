package com.deador.restshop.exception;

public class FileDeleteFailureException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String FILE_DELETE_FAILURE_EXCEPTION = "File delete failure exception";

    public FileDeleteFailureException() {
        super(FILE_DELETE_FAILURE_EXCEPTION);
    }

    public FileDeleteFailureException(String message) {
        super(message.isEmpty() ? FILE_DELETE_FAILURE_EXCEPTION : message);
    }
}
