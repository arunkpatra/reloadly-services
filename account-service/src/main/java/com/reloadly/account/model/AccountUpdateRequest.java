package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AccountUpdateRequest", description = "AccountUpdateRequest")
public class AccountUpdateRequest {

    @ApiModelProperty(value = "Name", required = true, position = 1)
    private final String name;
    @ApiModelProperty(value = "Email", required = true, position = 2)
    private final String email;
    @ApiModelProperty(value = "Phone number including Country code, e.g. +14159263478", required = true, position = 3)
    private final String phoneNumber;
    @ApiModelProperty(value = "Billing Address", required = true, position = 4)
    private final Address billingAddress;

    @JsonCreator
    public AccountUpdateRequest(String name, String email, String phoneNumber, Address billingAddress) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
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

    public Address getBillingAddress() {
        return billingAddress;
    }
}
