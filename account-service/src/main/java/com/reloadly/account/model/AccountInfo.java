package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * The account info. A short summary.
 *
 * @author Arun Patra
 */
@ApiModel(value = "AccountInfo", description = "AccountInfo")
public class AccountInfo {

    @ApiModelProperty(value = "Name", required = true, position = 1)
    private final String name;
    @ApiModelProperty(value = "Email", required = true, position = 2)
    private final String email;
    @ApiModelProperty(value = "Phone number including Country code, e.g. +14159263478", required = true, position = 3)
    private final String phoneNumber;

    @JsonCreator
    public AccountInfo(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
