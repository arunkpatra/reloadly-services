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
    public AccountUpdateRequest(@JsonProperty("accountInfo") AccountInfo accountInfo,
                                @JsonProperty("billingAddress") Address billingAddress) {
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
