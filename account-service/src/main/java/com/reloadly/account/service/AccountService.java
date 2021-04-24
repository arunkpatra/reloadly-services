package com.reloadly.account.service;

import com.reloadly.account.exception.AccountUpdatedException;
import com.reloadly.account.model.AccountUpdateRequest;
import com.reloadly.account.model.AccountUpdateResponse;

public interface AccountService {

    /**
     * Update an account. If an account does not exist, one will be created.
     * @param uid The UID of the user for whom this account is being created.
     * @param request The {@link AccountUpdateRequest} object.
     * @return A {@link AccountUpdateResponse} instance.
     * @throws AccountUpdatedException If account update fails.
     */
    AccountUpdateResponse updateAccount(String uid, AccountUpdateRequest request) throws AccountUpdatedException;
}
