package com.reloadly.account.controller;

import com.reloadly.account.exception.AccountBalanceException;
import com.reloadly.account.exception.AccountNotFoundException;
import com.reloadly.account.exception.AccountUpdateException;
import com.reloadly.commons.controller.BaseAbstractRestController;
import com.reloadly.commons.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An abstract REST Controller exposing useful facilities for concrete implementation to leverage.
 *
 * @author Arun Patra
 */
public abstract class AbstractRestController extends BaseAbstractRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountUpdateException.class)
    public ErrorResponse handleAccountUpdateException(AccountUpdateException e) {
        return new ErrorResponse("Failed to update account.", e.getMessage(), "");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountBalanceException.class)
    public ErrorResponse handleAccountBalanceException(AccountBalanceException e) {
        return new ErrorResponse("Failed to handle account balance.", e.getMessage(), "");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        return new ErrorResponse("Account not found.", e.getMessage(), "");
    }
}
