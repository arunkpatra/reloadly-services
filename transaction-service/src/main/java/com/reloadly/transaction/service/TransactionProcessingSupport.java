package com.reloadly.transaction.service;

import com.reloadly.security.model.ReloadlyUserDetails;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.AirtimeSendTxnEntity;
import com.reloadly.transaction.entity.MoneyReloadTxnEntity;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnException;
import com.reloadly.transaction.model.AddMoneyRequest;
import com.reloadly.transaction.model.SendAirtimeRequest;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.UUID;

public abstract class TransactionProcessingSupport extends KafkaSupport {

    protected final TransactionRepository transactionRepository;
    protected final MoneyReloadTxnRepository moneyReloadTxnRepository;
    protected final AirtimeSendTxnRepository airtimeSendTxnRepository;

    public TransactionProcessingSupport(TransactionRepository transactionRepository,
                                        MoneyReloadTxnRepository moneyReloadTxnRepository,
                                        AirtimeSendTxnRepository airtimeSendTxnRepository,
                                        TransactionServiceProperties properties,
                                        KafkaTemplate<String, String> template) {
        super(properties, template);
        this.transactionRepository = transactionRepository;
        this.moneyReloadTxnRepository = moneyReloadTxnRepository;
        this.airtimeSendTxnRepository = airtimeSendTxnRepository;
    }

    protected String getUid() throws ReloadlyTxnException {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(o instanceof ReloadlyUserDetails)) {
            throw new ReloadlyTxnException("Can not determine user ID");
        } else {
            return ((ReloadlyUserDetails) o).getUsername();
        }
    }

    protected TransactionEntity addTransactionRecord(String uid, TransactionRequest request) throws ReloadlyTxnException {
        try {
            TransactionEntity te = new TransactionEntity();
            te.setUid(uid);
            te.setTransactionType(request.getTransactionType());
            te.setTxnId(UUID.randomUUID().toString());
            te.setTransactionStatus(TransactionStatus.ACCEPTED);
            return transactionRepository.save(te);
        } catch (Exception e) {
            throw new ReloadlyTxnException("Could not register transaction record");
        }
    }

    protected void addMoneyReloadTxnRecord(TransactionEntity te, TransactionRequest request) throws ReloadlyTxnException {
        AddMoneyRequest req = request.getAddMoneyRequest();
        if (null == req) {
            throw new ReloadlyTxnException("Request did not contain money reload information");
        }
        Assert.notNull(req.getAmount(), "Amount can not be null");

        MoneyReloadTxnEntity mrte = new MoneyReloadTxnEntity();
        mrte.setTxnId(te.getTxnId());
        mrte.setAmount(request.getAddMoneyRequest().getAmount());
        moneyReloadTxnRepository.save(mrte);
    }

    protected void addSendAirtimeTxnRecord(TransactionEntity te, TransactionRequest request) throws ReloadlyTxnException {
        SendAirtimeRequest req = request.getSendAirtimeRequest();
        if (null == req) {
            throw new ReloadlyTxnException("Request did not contain airtime send information");
        }
        Assert.notNull(req.getAmount(), "Amount can not be null");
        Assert.notNull(req.getPhoneNumber(), "Phone number can not be null");

        AirtimeSendTxnEntity aste = new AirtimeSendTxnEntity();
        aste.setTxnId(te.getTxnId());
        aste.setAmount(req.getAmount());
        aste.setPhoneNumber(req.getPhoneNumber());
        airtimeSendTxnRepository.save(aste);
    }
}
