/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.account.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public Address(@JsonProperty("addressLine1") String addressLine1,
                   @JsonProperty("addressLine2") String addressLine2,
                   @JsonProperty("city") String city,
                   @JsonProperty("state") String state,
                   @JsonProperty("country") String country,
                   @JsonProperty("postalCode") String postalCode) {
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
