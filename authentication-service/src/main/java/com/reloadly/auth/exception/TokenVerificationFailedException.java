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
}
