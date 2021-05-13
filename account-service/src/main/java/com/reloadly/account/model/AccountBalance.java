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
    public AccountBalance(@JsonProperty("accountBalance") Float accountBalance, @JsonProperty("currencyCode") String currencyCode) {
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
