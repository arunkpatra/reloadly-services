package com.reloadly.transaction.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating that transaction processing has failed.
 *
 * @author Arun Patra
 */
public class ReloadlyTxnProcessingException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Could not process transaction";

    public ReloadlyTxnProcessingException() {
        super(DEFAULT_MESSAGE);
    }

    public ReloadlyTxnProcessingException(String message) {
        super(message);
    }

    public ReloadlyTxnProcessingException(Exception e) {
        super(DEFAULT_MESSAGE, e);
    }

    public ReloadlyTxnProcessingException(String message, Exception e) {
        super(message, e);
    }

}
