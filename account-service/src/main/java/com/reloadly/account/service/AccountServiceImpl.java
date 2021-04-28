package com.reloadly.account.service;

import com.reloadly.account.entity.AccountBalanceEntity;
import com.reloadly.account.entity.AccountEntity;
import com.reloadly.account.entity.AddressEntity;
import com.reloadly.account.exception.AccountBalanceException;
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

/**
 * Core capabilities of the Account microservice are exposed by the Account Service.
 *
 * @author Arun Patra
 */
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
        AccountEntity ae = accountRepository.findByUid(uid).orElseThrow(() -> new AccountNotFoundException("Account not found for user"));

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

    /**
     * Get Account info.
     *
     * @param uid The UID of the user.
     * @return The {@link AccountInfo} object.
     * @throws AccountNotFoundException If an error occurs.
     */
    @Override
    public AccountInfo getAccountInfo(String uid) throws AccountNotFoundException {
        Assert.notNull(uid, "UID can not be null");
        AccountEntity ae = accountRepository.findByUid(uid).orElseThrow(() -> new AccountNotFoundException("Account not found for user"));
        return new AccountInfo(ae.getName(), ae.getEmail(), ae.getPhoneNumber());
    }

    /**
     * Get account balance.
     *
     * @param uid The UID of the user.
     * @return The account balance.
     * @throws AccountBalanceException If an error occurs.
     */
    @Override
    public AccountBalance getAccountBalance(String uid) throws AccountBalanceException {

        AccountEntity ae = getAccountEntity(uid);
        AccountBalanceEntity abe = getAccountBalanceEntity(ae.getAccountId());

        return new AccountBalance(abe.getAccountBalance(), ae.getCurrencyCode());
    }

    /**
     * Credit an account.
     *
     * @param uid     The UID of the user.
     * @param request The {@link AccountCreditRequest} object.
     * @return The {@link AccountCreditResponse} object.
     * @throws AccountBalanceException If an error occurs.
     */
    @Override
    @Transactional
    public synchronized AccountCreditResponse creditAccountBalance(String uid, AccountCreditRequest request) throws AccountBalanceException {
        Assert.notNull(request.getAmount(), "Credit amount can not be null");

        AccountEntity ae = getAccountEntity(uid);
        AccountBalanceEntity abe = getAccountBalanceEntity(ae.getAccountId());
        abe.setAccountBalance(abe.getAccountBalance() + request.getAmount());

        try {
            accountBalanceRepository.save(abe);
        } catch (Exception e) {
            throw new AccountBalanceException("failed to credit transaction. Root cause: ".concat(e.getMessage()), e);
        }
        return new AccountCreditResponse("Account successfully credited");
    }

    /**
     * Debit an account.
     *
     * @param uid     The UID of the user.
     * @param request The {@link AccountDebitRequest} object.
     * @return The {@link AccountDebitResponse} object.
     * @throws AccountBalanceException If an error occurs.
     */
    @Override
    @Transactional
    public synchronized AccountDebitResponse debitAccountBalance(String uid, AccountDebitRequest request) throws AccountBalanceException {
        Assert.notNull(request.getAmount(), "debit amount can not be null");

        AccountEntity ae = getAccountEntity(uid);
        AccountBalanceEntity abe = getAccountBalanceEntity(ae.getAccountId());

        if (request.getAmount() > abe.getAccountBalance()) {
            throw new AccountBalanceException("Insufficient funds");
        }
        abe.setAccountBalance(abe.getAccountBalance() - request.getAmount());

        try {
            accountBalanceRepository.save(abe);
        } catch (Exception e) {
            throw new AccountBalanceException("failed to credit transaction. Root cause: ".concat(e.getMessage()), e);
        }
        return new AccountDebitResponse(true, "Account successfully debited");
    }

    private AccountEntity getAccountEntity(String uid) throws AccountBalanceException {
        return accountRepository.findByUid(uid).orElseThrow(() -> new AccountBalanceException("Account not found for user"));
    }

    private AccountBalanceEntity getAccountBalanceEntity(String accountId) throws AccountBalanceException {
        return accountBalanceRepository.findByAccountId(accountId).orElseThrow(() -> new AccountBalanceException("Account balance record not found"));
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
