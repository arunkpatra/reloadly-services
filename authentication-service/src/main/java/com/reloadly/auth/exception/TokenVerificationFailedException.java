package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;
/**
 * Exception indicating token verification failure.
 *
 * @author Arun Patra
 */
public class TokenVerificationFailedException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "Token verification failed";

    public TokenVerificationFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public TokenVerificationFailedException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public TokenVerificationFailedException(String message, Exception e) {
        super(message, e);
    }
}
