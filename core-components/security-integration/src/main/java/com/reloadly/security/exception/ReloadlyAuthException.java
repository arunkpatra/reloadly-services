package com.reloadly.security.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating an authentication error.
 *
 * @author Arun Patra
 */
public class ReloadlyAuthException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Auth failed";

    public ReloadlyAuthException() {
        super(DEFAULT_MESSAGE);
    }

    public ReloadlyAuthException(String message) {
        super(message);
    }

    public ReloadlyAuthException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public ReloadlyAuthException(String message, Exception e) {
        super(message, e);
    }

}
