package com.deador.restshop.exception;

public class FileTransferException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String FILE_TRANSFER_EXCEPTION = "File transfer exception";

    public FileTransferException() {
        super(FILE_TRANSFER_EXCEPTION);
    }

    public FileTransferException(String message) {
        super(message.isEmpty() ? FILE_TRANSFER_EXCEPTION : message);
    }
}
