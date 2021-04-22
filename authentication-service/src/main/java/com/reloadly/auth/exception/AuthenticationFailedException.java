package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class AuthenticationFailedException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The supplied credentials were incorrect.";

    public AuthenticationFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthenticationFailedException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public AuthenticationFailedException(String message, Exception e) {
        super(message, e);
    }
}
