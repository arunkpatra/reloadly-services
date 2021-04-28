package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Account credit response.
 *
 * @author Arun Patra
 */
public class AccountCreditResponse {

    private final String message;

    @JsonCreator
    public AccountCreditResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
