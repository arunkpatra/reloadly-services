package com.reloadly.transaction.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

public class ReloadlyTxnException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Could not process transaction";

    public ReloadlyTxnException() {
        super(DEFAULT_MESSAGE);
    }

    public ReloadlyTxnException(String message) {
        super(message);
    }

    public ReloadlyTxnException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public ReloadlyTxnException(String message, Exception e) {
        super(message, e);
    }

}
