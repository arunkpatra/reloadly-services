package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class UsernameNotFoundException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The username was not found.";

    public UsernameNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UsernameNotFoundException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }
}
