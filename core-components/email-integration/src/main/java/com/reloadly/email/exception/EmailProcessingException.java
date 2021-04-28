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

}
