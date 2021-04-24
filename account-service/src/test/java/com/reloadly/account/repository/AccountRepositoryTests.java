package com.reloadly.account.repository;

import com.reloadly.account.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void should_find_account_by_acct_id() {
        String existingAccountId = "f1fe6f0d-420e-4161-a134-9c2342e36c99";
        assertThat(accountRepository.findByAccountId(existingAccountId).isPresent()).isTrue();
    }

    @Test
    public void should_not_find_non_existent_acct() {
        String nonExistentAccountId = "e1fe6f0d-420e-4161-a134-9c2342e36c99";
        assertThat(accountRepository.findByAccountId(nonExistentAccountId).isPresent()).isFalse();
    }

}
