package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating that the username was not found.
 *
 * @author Arun Patra
 */
public class ApiKeyNotFoundException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "Use was not found.";

    public ApiKeyNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

}
