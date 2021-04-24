package com.reloadly.account.repository;

import com.reloadly.account.AbstractIntegrationTest;
import com.reloadly.account.entity.AccountBalanceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountBalanceRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    @Test
    public void should_find_account_bal_by_acct_bal_id() {
        String accountId = "f1fe6f0d-420e-4161-a134-9c2342e36c99";
        Optional<AccountBalanceEntity> optAbe = accountBalanceRepository.findByAccountId(accountId);
        assertThat(optAbe.isPresent()).isTrue();
        assertThat(optAbe.get().getAccountBalance()).isEqualTo(200.5f);
    }

}
