package com.deador.restshop.exception;

public class DirectoryCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String DIRECTORY_CREATION_EXCEPTION = "Directory creation error";

    public DirectoryCreationException() {
        super(DIRECTORY_CREATION_EXCEPTION);
    }

    public DirectoryCreationException(String message) {
        super(message.isEmpty() ? DIRECTORY_CREATION_EXCEPTION : message);
    }
}
