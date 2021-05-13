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

package com.reloadly.account.service;

import com.reloadly.account.exception.AccountBalanceException;
import com.reloadly.account.exception.AccountNotFoundException;
import com.reloadly.account.exception.AccountUpdateException;
import com.reloadly.account.model.AccountBalance;
import com.reloadly.account.model.AccountDetails;
import com.reloadly.account.model.AccountUpdateRequest;
import com.reloadly.account.model.AccountUpdateResponse;
import com.reloadly.commons.model.account.*;

/**
 * Core capabilities of the Account microservice are exposed by the Account Service.
 *
 * @author Arun Patra
 */
public interface AccountService {

    /**
     * Update an account. If an account does not exist, one will be created.
     *
     * @param uid     The UID of the user for whom this account is being created.
     * @param request The {@link AccountUpdateRequest} object.
     * @return A {@link AccountUpdateResponse} instance.
     * @throws AccountUpdateException If account update fails.
     */
    AccountUpdateResponse updateAccount(String uid, AccountUpdateRequest request) throws AccountUpdateException;

    /**
     * Locates the account for the specified user and returns it.
     *
     * @param uid The UID of the user.
     * @return The account details object.
     * @throws AccountNotFoundException If no account was found for the user.
     */
    AccountDetails getAccountDetails(String uid) throws AccountNotFoundException;

    /**
     * Get account balance.
     *
     * @param uid The UID of the user.
     * @return The account balance.
     * @throws AccountBalanceException If an error occurs.
     */
    AccountBalance getAccountBalance(String uid) throws AccountBalanceException;

    /**
     * Credit an account.
     *
     * @param uid     The UID of the user.
     * @param request The {@link AccountCreditReq} object.
     * @return The {@link AccountCreditResp} object.
     * @throws AccountBalanceException If an error occurs.
     */
    AccountCreditResp creditAccountBalance(String uid, AccountCreditReq request) throws AccountBalanceException;

    /**
     * Debit an account.
     *
     * @param uid     The UID of the user.
     * @param request The {@link AccountDebitReq} object.
     * @return The {@link AccountDebitResp} object.
     * @throws AccountBalanceException If an error occurs.
     */
    AccountDebitResp debitAccountBalance(String uid, AccountDebitReq request) throws AccountBalanceException;

    /**
     * Get Account info.
     *
     * @param uid The UID of the user.
     * @return The {@link AccountInfo} object.
     * @throws AccountNotFoundException If an error occurs.
     */
    AccountInfo getAccountInfo(String uid) throws AccountNotFoundException;
}
