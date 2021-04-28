package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating that the username was not found.
 *
 * @author Arun Patra
 */
public class UsernameNotFoundException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The username was not found.";

    public UsernameNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

}
