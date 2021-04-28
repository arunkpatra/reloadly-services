package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating that a username is already taken.
 *
 * @author Arun Patra
 */
public class UsernameAlreadyTakenException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The username has already been taken.";

    public UsernameAlreadyTakenException() {
        super(DEFAULT_MESSAGE);
    }

    public UsernameAlreadyTakenException(String message, Exception e) {
        super(message, e);
    }
}
