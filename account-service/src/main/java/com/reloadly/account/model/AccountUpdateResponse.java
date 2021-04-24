package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AccountUpdateResponse", description = "AccountUpdateResponse")
public class AccountUpdateResponse {
    @ApiModelProperty(value = "Successful", required = true, position = 1)
    private final Boolean successful;
    @ApiModelProperty(value = "Message", position = 2)
    private final String message;

    @JsonCreator
    public AccountUpdateResponse(Boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}
