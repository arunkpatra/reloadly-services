package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating authentication failure.
 *
 * @author Arun Patra
 */
public class AuthenticationFailedException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The supplied credentials were incorrect.";

    public AuthenticationFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
