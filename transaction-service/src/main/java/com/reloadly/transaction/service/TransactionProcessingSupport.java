package com.reloadly.transaction.service;

import com.reloadly.security.auth.model.ReloadlyUserDetails;
import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnException;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

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

    }

    protected void addSendAirtimeTxnRecord(TransactionEntity te, TransactionRequest request) throws ReloadlyTxnException {

    }
}
