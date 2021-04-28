package com.reloadly.account.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating invalid balance amounts for the requested operation, e.g. a debit operation while existing
 * balance is insufficient.
 *
 * @author Arun Patra
 */
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
