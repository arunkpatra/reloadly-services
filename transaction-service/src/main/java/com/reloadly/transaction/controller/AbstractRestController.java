package com.reloadly.transaction.controller;

import com.reloadly.commons.controller.BaseAbstractRestController;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.transaction.exception.ReloadlyTxnSvcException;
import com.reloadly.transaction.model.TransactionResponse;
import com.reloadly.transaction.model.TransactionStatus;
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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ReloadlyTxnSvcException.class)
    public TransactionResponse handleReloadlyTxnSvcException(ReloadlyTxnSvcException e) {
        return new TransactionResponse("", TransactionStatus.REJECTED);
    }
}
