package com.reloadly.account.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class AccountBalanceException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Unable to process request";

    public AccountBalanceException() {
        super(DEFAULT_MESSAGE);
    }

    public AccountBalanceException(String message) {
        super(message);
    }

    public AccountBalanceException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public AccountBalanceException(String message, Exception e) {
        super(message, e);
    }

}
