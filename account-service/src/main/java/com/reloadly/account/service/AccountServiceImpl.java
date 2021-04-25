package com.reloadly.account.service;

import com.reloadly.account.entity.AccountBalanceEntity;
import com.reloadly.account.entity.AccountEntity;
import com.reloadly.account.entity.AddressEntity;
import com.reloadly.account.exception.AccountNotFoundException;
import com.reloadly.account.exception.AccountUpdatedException;
import com.reloadly.account.model.*;
import com.reloadly.account.repository.AccountBalanceRepository;
import com.reloadly.account.repository.AccountRepository;
import com.reloadly.account.repository.AddressRepository;
import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl extends AccountUpdateNotificationSupport implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final AddressRepository addressRepository;

    public AccountServiceImpl(AccountRepository accountRepository, AccountBalanceRepository accountBalanceRepository,
                              AddressRepository addressRepository, ReloadlyNotification notification) {
        super(notification);
        this.accountRepository = accountRepository;
        this.accountBalanceRepository = accountBalanceRepository;
        this.addressRepository = addressRepository;
    }

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
    @Transactional
    public AccountUpdateResponse updateAccount(String uid, AccountUpdateRequest request) throws AccountUpdatedException {

        try {
            boolean newAccount = isNewAccount(uid);

            // Update base account details
            AccountEntity ae = updateBaseAccountDetails(uid, request);

            // Add balance record if needed
            createAccountBalanceRecordIfNeeded(ae);

            // Update billing address
            saveBillingAddress(ae, request.getBillingAddress());

            // Notify
            sendAccountUpdateNotifications(ae, newAccount);

            return new AccountUpdateResponse(true, ae.getAccountId(), "Account update successful");
        } catch (Exception e) {
            throw new AccountUpdatedException("Account update failed. Root cause: " + e.getMessage(), e);
        }
    }

    /**
     * Locates the account for the specified user and returns it.
     *
     * @param uid The UID of the user.
     * @return The account details object.
     * @throws AccountNotFoundException If no account was founnd for the user.
     */
    @Override
    public AccountDetails getAccountDetails(String uid) throws AccountNotFoundException {
        Assert.notNull(uid, "UID can not be null");
        Optional<AccountEntity> aeOptional = accountRepository.findByUid(uid);

        if (!aeOptional.isPresent()) {
            throw new AccountNotFoundException();
        }

        AccountEntity ae = aeOptional.get();
        AccountInfo accountInfo = new AccountInfo(ae.getName(), ae.getEmail(), ae.getPhoneNumber());
        AccountBalance accountBalance = new AccountBalance(ae.getAccountBalanceEntity().getAccountBalance(), ae.getCurrencyCode());
        Address billingAddress = null;
        Optional<AddressEntity> addrOpt = addressRepository.findByAccountIdAndAddressType(ae.getAccountId(), AddressType.BILLING);
        if (addrOpt.isPresent()) {
            billingAddress = new Address(addrOpt.get().getAddressLine1(), addrOpt.get().getAddressLine2(),
                    addrOpt.get().getCity(), addrOpt.get().getState(), addrOpt.get().getCountry(), addrOpt.get().getPostalCode());
        }

        return new AccountDetails(ae.getAccountId(), accountInfo, accountBalance, billingAddress);
    }

    private AccountEntity updateBaseAccountDetails(String uid, AccountUpdateRequest request) {
        Assert.notNull(uid, "UID can not be null");
        Assert.notNull(request.getAccountInfo(), "Account Info can not be null");

        // If account does not exist, instantiate a new one.
        AccountEntity ae = accountRepository.findByUid(uid).orElseGet(AccountEntity::new);

        // First update account details.
        ae.setUid(uid);
        if (null == ae.getAccountId()) {
            ae.setAccountId(UUID.randomUUID().toString());
        }
        ae.setName(request.getAccountInfo().getName());
        ae.setPhoneNumber(request.getAccountInfo().getPhoneNumber());
        ae.setEmail(request.getAccountInfo().getEmail());
        if (null == ae.getActive()) {
            ae.setActive(true);
        }
        ae.setCurrencyCode("USD"); // Default, can be changed later based on billing address
        // now save
        return accountRepository.save(ae);
    }

    private void createAccountBalanceRecordIfNeeded(AccountEntity ae) {
        Optional<AccountBalanceEntity> abeOpt = accountBalanceRepository.findByAccountId(ae.getAccountId());
        if (!abeOpt.isPresent()) {
            AccountBalanceEntity abe = new AccountBalanceEntity();
            abe.setAccountId(ae.getAccountId());
            abe.setAccountBalance(0.0f); // default
            accountBalanceRepository.save(abe);
        }
    }

    private void saveBillingAddress(AccountEntity ae, Address billingAddress) {
        // Update billing address
        if (null != billingAddress) {
            // If billing address does not exist, create one.
            AddressEntity adr = addressRepository.findByAccountIdAndAddressType(ae.getAccountId(), AddressType.BILLING)
                    .orElseGet(AddressEntity::new);
            if (null == adr.getAccountId()) {
                adr.setAccountId(ae.getAccountId());
            }
            adr.setAddressType(AddressType.BILLING);
            adr.setPostalCode(billingAddress.getPostalCode());
            adr.setCountry(billingAddress.getCountry());
            adr.setState(billingAddress.getState());
            adr.setCity(billingAddress.getCity());
            adr.setAddressLine1(billingAddress.getAddressLine1());
            adr.setAddressLine2(billingAddress.getAddressLine2());
            // save
            addressRepository.save(adr);
        }
    }

    private boolean isNewAccount(String uid) {
        return accountRepository.findByUid(uid).isPresent();
    }
}
