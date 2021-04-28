package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating invalid password format.
 *
 * @author Arun Patra
 */
public class InvalidPasswordFormatException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "The password is less than eight characters.";

    public InvalidPasswordFormatException() {
        super(DEFAULT_MESSAGE);
    }

}
