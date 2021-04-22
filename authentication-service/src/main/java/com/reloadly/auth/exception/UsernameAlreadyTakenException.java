package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class UsernameAlreadyTakenException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The username has already been taken.";

    public UsernameAlreadyTakenException() {
        super(DEFAULT_MESSAGE);
    }

    public UsernameAlreadyTakenException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public UsernameAlreadyTakenException(String message, Exception e) {
        super(message, e);
    }
}
