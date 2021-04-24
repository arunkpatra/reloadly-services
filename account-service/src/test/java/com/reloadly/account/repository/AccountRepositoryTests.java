package com.reloadly.account.repository;

import com.reloadly.account.AbstractIntegrationTest;
import com.reloadly.account.entity.AccountBalanceEntity;
import com.reloadly.account.entity.AccountEntity;
import com.reloadly.account.entity.AddressEntity;
import com.reloadly.account.model.AddressType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void should_find_account_by_acct_id() {
        String existingAccountId = "f1fe6f0d-420e-4161-a134-9c2342e36c99";
        Optional<AccountEntity> account = accountRepository.findByAccountId(existingAccountId);

        assertThat(account.isPresent()).isTrue();
        assertThat(account.get().getAccountBalanceEntity().getAccountBalance()).isEqualTo(200.5f);
        assertThat(account.get().getAddressEntities().size()).isEqualTo(1);
        assertThat(account.get().getAddressEntities().get(0)).isNotNull();
        assertThat(account.get().getAddressEntities().get(0).getAddressType()).isEqualTo(AddressType.BILLING);
    }

    @Test
    @Transactional
    @Rollback
    public void should_create_new_account() {
        String newAccountId = "e1fe6f0d-420e-4161-a134-9c2342e36c97";
        String someUid = "some-unique-uid-123";
        AccountEntity ae = new AccountEntity();
        ae.setUid(someUid);
        ae.setAccountId(newAccountId);
        ae.setActive(true);
        ae.setEmail("testx123@email.com");
        ae.setPhoneNumber("+9188888888");
        ae.setName("Jane Doe");
        ae.setCurrencyCode("USD");
        // Save
        ae = accountRepository.save(ae);
        assertThat(ae).isNotNull();

        // Now add a balance record
        AccountBalanceEntity abe = new AccountBalanceEntity();
        abe.setAccountId(ae.getAccountId());
        abe.setAccountBalance(120.67f);

        abe = accountBalanceRepository.save(abe);
        assertThat(abe).isNotNull();

        // Now add a billing address
        AddressEntity adr = new AddressEntity();
        adr.setAccountId(ae.getAccountId());
        adr.setAddressLine1("Any adr 1");
        adr.setAddressLine2("Any adr 2");
        adr.setCity("Bangalore");
        adr.setState("Karnataka");
        adr.setCountry("India");
        adr.setPostalCode("123456789");
        adr.setAddressType(AddressType.BILLING);

        adr = addressRepository.save(adr);
        assertThat(adr).isNotNull();

        // Now get back account record again
        Optional<AccountEntity> aeOptional = accountRepository.findByUid(someUid);
        assertThat(aeOptional.isPresent()).isTrue();
    }

    @Test
    public void should_not_find_non_existent_acct() {
        String nonExistentAccountId = "e1fe6f0d-420e-4161-a134-9c2342e36c99";
        assertThat(accountRepository.findByAccountId(nonExistentAccountId).isPresent()).isFalse();
    }
}
