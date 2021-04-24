package com.reloadly.account.service;

import com.reloadly.account.exception.AccountUpdatedException;
import com.reloadly.account.model.AccountUpdateRequest;
import com.reloadly.account.model.AccountUpdateResponse;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    /**
     * Update an account. If an account does not exist, one will be created. Note that, if billing address is provided
     * it is either created or updated.
     *
     * @param uid     The UID of the user for whom this account is being created.
     * @param request The {@link AccountUpdateRequest} object.
     * @return A {@link AccountUpdateResponse} instance.
     * @throws AccountUpdatedException If account update fails.
     */
    @Override
    public AccountUpdateResponse updateAccount(String uid, AccountUpdateRequest request) throws AccountUpdatedException {
        return null;
    }
}
