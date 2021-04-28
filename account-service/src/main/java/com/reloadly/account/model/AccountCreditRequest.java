package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Account credit request.
 *
 * @author Arun Patra
 */
public class AccountCreditRequest {

    private final Float amount;

    @JsonCreator
    public AccountCreditRequest(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
