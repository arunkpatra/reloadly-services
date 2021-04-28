package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Account debit request.
 *
 * @author Arun Patra
 */
public class AccountDebitRequest {

    private final Float amount;

    @JsonCreator
    public AccountDebitRequest(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
