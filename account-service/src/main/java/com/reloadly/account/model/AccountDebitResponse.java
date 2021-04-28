package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * The account debit response.
 *
 * @author Arun Patra
 */
public class AccountDebitResponse {

    private final Boolean successful;
    private final String message;

    @JsonCreator
    public AccountDebitResponse(Boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccessful() {
        return successful;
    }
}
