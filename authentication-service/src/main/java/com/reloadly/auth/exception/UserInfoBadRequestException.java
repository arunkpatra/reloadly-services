package com.reloadly.auth.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating that the username was not found.
 *
 * @author Arun Patra
 */
public class UserInfoBadRequestException extends ReloadlyException {
    private static final String DEFAULT_MESSAGE = "Request must contain the RELOADLY-API-KEY, Authorization headers.";

    public UserInfoBadRequestException() {
        super(DEFAULT_MESSAGE);
    }

}
