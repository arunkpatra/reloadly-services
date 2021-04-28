package com.reloadly.transaction.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating transaction acceptance failure.
 *
 * @author Arun Patra
 */
public class ReloadlyTxnSvcException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Could not process transaction";

    public ReloadlyTxnSvcException() {
        super(DEFAULT_MESSAGE);
    }

    public ReloadlyTxnSvcException(String message) {
        super(message);
    }

    public ReloadlyTxnSvcException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public ReloadlyTxnSvcException(String message, Exception e) {
        super(message, e);
    }

}
