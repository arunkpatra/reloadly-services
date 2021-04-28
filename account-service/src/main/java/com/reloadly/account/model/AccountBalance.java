package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * Account balance.
 *
 * @author Arun Patra
 */
@ApiModel(value = "AccountInfo", description = "AccountInfo")
public class AccountBalance {

    @ApiModelProperty(value = "Account Balance", required = true, position = 1)
    private final Float accountBalance;

    @ApiModelProperty(value = "Currency Code", required = true, position = 1)
    private final String currencyCode;

    @JsonCreator
    public AccountBalance(Float accountBalance, String currencyCode) {
        this.accountBalance = accountBalance;
        this.currencyCode = currencyCode;
    }

    public Float getAccountBalance() {
        return accountBalance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
