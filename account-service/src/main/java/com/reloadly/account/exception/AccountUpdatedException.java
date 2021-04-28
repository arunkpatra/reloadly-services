package com.reloadly.account.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating that account update has encountered errors.
 *
 * @author Arun Patra
 */
public class AccountUpdatedException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Account update failed";

    public AccountUpdatedException(String message, Exception e) {
        super(message, e);
    }

}
