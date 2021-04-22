package com.reloadly.commons.exceptions;

public class ReloadlyException extends Exception {
    private static final String defaultMessage = "An error occurred";

    public ReloadlyException() {
        super(defaultMessage);
    }

    public ReloadlyException(String message) {
        super(message);
    }

    public ReloadlyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReloadlyException(Throwable cause) {
        super(defaultMessage, cause);
    }

    public ReloadlyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}