package com.reloadly.transaction.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class AccountInfoRetrievalException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Could not process transaction";

    public AccountInfoRetrievalException() {
        super(DEFAULT_MESSAGE);
    }

    public AccountInfoRetrievalException(String message) {
        super(message);
    }

    public AccountInfoRetrievalException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public AccountInfoRetrievalException(String message, Exception e) {
        super(message, e);
    }

}
