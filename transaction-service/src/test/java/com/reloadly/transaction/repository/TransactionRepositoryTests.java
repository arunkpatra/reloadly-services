package com.reloadly.transaction.repository;

import com.reloadly.transaction.AbstractIntegrationTest;
import com.reloadly.transaction.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Transactional
    public void should_find_txn_by_txn_id() {
        // This is a money reload txn
        String existingTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        Optional<TransactionEntity> txnOpt = transactionRepository.findByTxnId(existingTxnId);
        assertThat(txnOpt.isPresent()).isTrue();
        TransactionEntity te = txnOpt.get();
        assertThat(te.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        assertThat(txnOpt.get().getAirtimeSendTxnEntities().size()).isEqualTo(0);
        assertThat(txnOpt.get().getMoneyReloadTxnEntities().size()).isEqualTo(1);


        // This is a airtime send  txn
        String existingTxnIdAirtime = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        txnOpt = transactionRepository.findByTxnId(existingTxnIdAirtime);
        assertThat(txnOpt.isPresent()).isTrue();
        te = txnOpt.get();
        assertThat(te.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        assertThat(txnOpt.get().getMoneyReloadTxnEntities().size()).isEqualTo(0);
        assertThat(txnOpt.get().getAirtimeSendTxnEntities().size()).isEqualTo(1);

    }

    @Test
    @Transactional
    public void should_find_all_txn_by_uid() {
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        List<TransactionEntity> txnList = transactionRepository.findByUid(uid);
        assertThat(txnList.size()).isEqualTo(2);
    }

    @Test
    public void should_not_find_non_existent_txn() {
        String nonExistentTxn = "f7fe6f0d-420e-5161-a135-9c2342e36c34";
        assertThat(transactionRepository.findByTxnId(nonExistentTxn).isPresent()).isFalse();
    }

}
