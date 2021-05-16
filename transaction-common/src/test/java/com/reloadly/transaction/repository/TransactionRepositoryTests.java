/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.transaction.repository;

import com.reloadly.transaction.AbstractJpaIntegrationTest;
import com.reloadly.transaction.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionRepositoryTests extends AbstractJpaIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MoneyReloadTxnRepository moneyReloadTxnRepository;

    @Autowired
    private AirtimeSendTxnRepository airtimeSendTxnRepository;

    @Test
    @Transactional
    public void should_find_txn_by_txn_id() {
        // This is a money reload txn
        String existingTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        Optional<TransactionEntity> txnOpt = transactionRepository.findByTxnId(existingTxnId);
        assertThat(txnOpt.isPresent()).isTrue();
        TransactionEntity te = txnOpt.get();
        assertThat(te.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        assertThat(moneyReloadTxnRepository.getByTxnId(existingTxnId).get().getTxnId()).isNotNull();

        // This is a airtime send  txn
        String existingTxnIdAirtime = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        txnOpt = transactionRepository.findByTxnId(existingTxnIdAirtime);
        assertThat(txnOpt.isPresent()).isTrue();
        te = txnOpt.get();
        assertThat(te.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        assertThat(airtimeSendTxnRepository.getByTxnId(existingTxnIdAirtime).get().getTxnId()).isNotNull();

    }

    @Test
    @Transactional
    public void should_find_all_txn_by_uid() {
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        List<TransactionEntity> txnList = transactionRepository.findByUid(uid);
        assertThat(txnList.size()).isGreaterThan(0);
    }

    @Test
    public void should_not_find_non_existent_txn() {
        String nonExistentTxn = "f7fe6f0d-420e-5161-a135-9c2342e36c34";
        assertThat(transactionRepository.findByTxnId(nonExistentTxn).isPresent()).isFalse();
    }

}
