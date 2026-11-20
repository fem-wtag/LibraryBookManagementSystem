package com.library.exception;

public class LibraryException extends RuntimeException{

    public enum ErrorType { NOT_FOUND, UNAVAILABLE, INVALID_INPUT, STORAGE_ERROR }

    private final ErrorType errorType;

    public LibraryException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public LibraryException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getFullCasualChain() {
        StringBuilder sb = new StringBuilder("[" + errorType + "]" + getMessage());
        Throwable cause = getCause();
        while (cause != null) {
            sb.append("\n  ↳ Caused by")
                    .append(cause.getClass().getSimpleName())
                    .append(": ")
                    .append(cause.getMessage());
            cause = cause.getCause();
        }
        return sb.toString();
    }
}
