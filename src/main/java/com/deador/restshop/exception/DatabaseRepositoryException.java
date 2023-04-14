package com.deador.restshop.exception;

public class DatabaseRepositoryException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private static final String DATABASE_REPOSITORY_EXCEPTION = "Database repository exception";

    public DatabaseRepositoryException() {
        super(DATABASE_REPOSITORY_EXCEPTION);
    }

    public DatabaseRepositoryException(String message) {
        super(message.isEmpty() ? DATABASE_REPOSITORY_EXCEPTION : message);
    }
}
