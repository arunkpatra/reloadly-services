package com.reloadly.account.repository;

import com.reloadly.account.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountBalanceRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Test
    public void should_find_account_bal_by_acct_bal_id() {
        String acctBalanceUid = "d1fe6f0d-420e-4161-a134-9c2342e36c88";
        assertThat(accountBalanceRepository.findByAcctBalanceUid(acctBalanceUid).isPresent()).isTrue();
    }

}
