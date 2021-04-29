package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.reloadly.commons.model.account.AccountInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The account update request.
 *
 * @author Arun Patra
 */
@ApiModel(value = "AccountUpdateRequest", description = "AccountUpdateRequest")
public class AccountUpdateRequest {

    @ApiModelProperty(value = "Account Info", required = true, position = 1)
    private final AccountInfo accountInfo;

    @ApiModelProperty(value = "Billing Address", required = true, position = 2)
    private final Address billingAddress;

    @JsonCreator
    public AccountUpdateRequest(AccountInfo accountInfo, Address billingAddress) {
        this.accountInfo = accountInfo;
        this.billingAddress = billingAddress;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }
}
