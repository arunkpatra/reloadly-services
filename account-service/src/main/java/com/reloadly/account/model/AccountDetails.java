package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reloadly.commons.model.account.AccountInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The account details.
 *
 * @author Arun Patra
 */
@ApiModel(value = "AccountDetails", description = "AccountDetails")
public class AccountDetails {

    @ApiModelProperty(value = "Account ID", position = 1)
    private final String accountId;
    @ApiModelProperty(value = "Account Info", position = 2)
    private final AccountInfo accountInfo;
    @ApiModelProperty(value = "Account Balance", position = 3)
    private final AccountBalance balance;
    @ApiModelProperty(value = "Billing Address", position = 4)
    private final Address billingAddress;

    @JsonCreator
    public AccountDetails(@JsonProperty("accountId") String accountId,
                          @JsonProperty("accountInfo") AccountInfo accountInfo,
                          @JsonProperty("balance") AccountBalance balance,
                          @JsonProperty("billingAddress") Address billingAddress) {
        this.accountId = accountId;
        this.accountInfo = accountInfo;
        this.balance = balance;
        this.billingAddress = billingAddress;
    }

    public String getAccountId() {
        return accountId;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public AccountBalance getBalance() {
        return balance;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }
}
