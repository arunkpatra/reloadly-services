package com.reloadly.email.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * An exception reserved exclusively for handling errors related to Email processing.
 *
 * @author Arun Patra
 */
public class EmailProcessingException extends ReloadlyException {
    public EmailProcessingException() {
    }

    public EmailProcessingException(String message) {
        super(message);
    }

    public EmailProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailProcessingException(Throwable cause) {
        super(cause);
    }

    public EmailProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
