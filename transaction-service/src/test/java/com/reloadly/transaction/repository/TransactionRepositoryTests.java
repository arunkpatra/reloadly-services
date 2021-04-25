package com.reloadly.transaction.repository;

import com.reloadly.transaction.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void should_find_txn_by_txn_id() {
        String existingTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        assertThat(transactionRepository.findByTxnId(existingTxnId).isPresent()).isTrue();
    }

    @Test
    public void should_not_find_non_existent_txn() {
        String nonExistentTxn = "f7fe6f0d-420e-5161-a135-9c2342e36c34";
        assertThat(transactionRepository.findByTxnId(nonExistentTxn).isPresent()).isFalse();
    }

}
