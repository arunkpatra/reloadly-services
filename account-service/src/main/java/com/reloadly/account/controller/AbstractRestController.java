package com.reloadly.account.controller;

import com.reloadly.account.exception.AccountBalanceException;
import com.reloadly.account.exception.AccountNotFoundException;
import com.reloadly.account.exception.AccountUpdateException;
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
public abstract class AbstractRestController {

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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception e) {
        String message = "An error occurred";
        String errorDetail = extractMessage(e);
        LOGGER.error("Error occurred: description={}, detail={}", message, errorDetail);
        return new ErrorResponse("An error was encountered.", message, errorDetail);
    }

    private String extractMessage(Exception e) {
        String message = "";
        if (null != e.getMessage()) {
            message = e.getMessage();
        } else {
            // does it have a cause?
            if (null != e.getCause()) {
                if (null != e.getCause().getMessage()) {
                    message = e.getCause().getMessage();
                }
            }
        }
        return message;
    }
}
