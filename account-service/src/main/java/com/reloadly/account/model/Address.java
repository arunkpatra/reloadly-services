package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The address of an entity.
 *
 * @author Arun Patra
 */
@ApiModel(value = "Address", description = "Address")
public class Address {

    @ApiModelProperty(value = "Address Line 1", required = true, position = 1)
    private final String addressLine1;
    @ApiModelProperty(value = "Address Line 2", required = true, position = 2)
    private final String addressLine2;
    @ApiModelProperty(value = "City", required = true, position = 3)
    private final String city;
    @ApiModelProperty(value = "State", required = true, position = 4)
    private final String state;
    @ApiModelProperty(value = "Country", required = true, position = 5)
    private final String country;
    @ApiModelProperty(value = "Postal Code", required = true, position = 6)
    private final String postalCode;

    @JsonCreator
    public Address(String addressLine1, String addressLine2, String city, String state, String country, String postalCode) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
