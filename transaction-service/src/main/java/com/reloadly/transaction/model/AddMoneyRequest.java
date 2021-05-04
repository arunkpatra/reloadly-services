package com.reloadly.transaction.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Add money request.
 *
 * @author Arun Patra
 */
public class AddMoneyRequest {

    private final Float amount;

    @JsonCreator
    public AddMoneyRequest(@JsonProperty("amount") Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
