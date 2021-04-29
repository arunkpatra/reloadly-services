package com.reloadly.transaction.exception;

import com.reloadly.commons.exceptions.ReloadlyException;

/**
 * Exception indicating account information retrieval failure.
 *
 * @author Arun Patra
 */
public class AccountInfoRetrievalException extends ReloadlyException {

    private static final String DEFAULT_MESSAGE = "Could not process transaction";

    public AccountInfoRetrievalException(String message) {
        super(message);
    }
}
