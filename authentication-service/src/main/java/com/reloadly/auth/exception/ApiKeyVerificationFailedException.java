package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating API key verification failure.
 *
 * @author Arun Patra
 */
public class ApiKeyVerificationFailedException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "API Key verification failed";

    public ApiKeyVerificationFailedException(String message) {
        super(message);
    }
}
