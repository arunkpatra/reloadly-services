package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * The account update response.
 *
 * @author Arun Patra
 */
@ApiModel(value = "AccountUpdateResponse", description = "AccountUpdateResponse")
public class AccountUpdateResponse {
    @ApiModelProperty(value = "Successful", required = true, position = 1)
    private final Boolean successful;
    @ApiModelProperty(value = "Account ID", position = 2)
    private final String accountId;
    @ApiModelProperty(value = "Message", position = 3)
    private final String message;

    @JsonCreator
    public AccountUpdateResponse(Boolean successful, String accountId, String message) {
        this.successful = successful;
        this.accountId = accountId;
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public String getAccountId() {
        return accountId;
    }
}
