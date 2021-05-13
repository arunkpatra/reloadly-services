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

package com.reloadly.transaction.service;

import com.reloadly.security.model.ReloadlyUserDetails;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.AirtimeSendTxnEntity;
import com.reloadly.transaction.entity.MoneyReloadTxnEntity;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnSvcException;
import com.reloadly.transaction.model.AddMoneyRequest;
import com.reloadly.transaction.model.SendAirtimeRequest;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Various transaction processing support functions..
 *
 * @author Arun Patra
 */
public abstract class TransactionProcessingSupport extends KafkaSupport {

    protected final TransactionRepository transactionRepository;
    protected final MoneyReloadTxnRepository moneyReloadTxnRepository;
    protected final AirtimeSendTxnRepository airtimeSendTxnRepository;

    public TransactionProcessingSupport(TransactionRepository transactionRepository,
                                        MoneyReloadTxnRepository moneyReloadTxnRepository,
                                        AirtimeSendTxnRepository airtimeSendTxnRepository,
                                        TransactionServiceProperties properties,
                                        KafkaTemplate<String, String> template,
                                        ApplicationContext context) {
        super(properties, template, context);
        this.transactionRepository = transactionRepository;
        this.moneyReloadTxnRepository = moneyReloadTxnRepository;
        this.airtimeSendTxnRepository = airtimeSendTxnRepository;
    }

    protected String getUid() {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((ReloadlyUserDetails) o).getUsername();
    }

    protected TransactionEntity addTransactionRecord(String uid, TransactionRequest request) {

        TransactionEntity te = new TransactionEntity();
        te.setUid(uid);
        te.setTransactionType(request.getTransactionType());
        te.setTxnId(UUID.randomUUID().toString());
        te.setTransactionStatus(TransactionStatus.ACCEPTED);
        return transactionRepository.save(te);

    }

    protected void addMoneyReloadTxnRecord(TransactionEntity te, TransactionRequest request) throws ReloadlyTxnSvcException {
        AddMoneyRequest req = request.getAddMoneyRequest();
        Assert.notNull(req, "Request did not contain money reload information");
        Assert.notNull(req.getAmount(), "Amount can not be null");

        MoneyReloadTxnEntity mrte = new MoneyReloadTxnEntity();
        mrte.setTxnId(te.getTxnId());
        mrte.setAmount(request.getAddMoneyRequest().getAmount());
        moneyReloadTxnRepository.save(mrte);
    }

    protected void addSendAirtimeTxnRecord(TransactionEntity te, TransactionRequest request) throws ReloadlyTxnSvcException {
        SendAirtimeRequest req = request.getSendAirtimeRequest();
        Assert.notNull(req, "Request did not contain airtime send information");
        Assert.notNull(req.getAmount(), "Amount can not be null");
        Assert.notNull(req.getPhoneNumber(), "Phone number can not be null");

        AirtimeSendTxnEntity aste = new AirtimeSendTxnEntity();
        aste.setTxnId(te.getTxnId());
        aste.setAmount(req.getAmount());
        aste.setPhoneNumber(req.getPhoneNumber());
        airtimeSendTxnRepository.save(aste);
    }
}
