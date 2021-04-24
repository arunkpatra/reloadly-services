package com.reloadly.account.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class AccountUpdatedException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Account update failed";

    public AccountUpdatedException() {
        super(DEFAULT_MESSAGE);
    }

    public AccountUpdatedException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public AccountUpdatedException(String message, Exception e) {
        super(message, e);
    }

}
