package com.reloadly.transaction.service;

import com.reloadly.transaction.config.TransactionServiceProperties;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.KafkaProcessingException;
import com.reloadly.transaction.exception.ReloadlyTxnException;
import com.reloadly.transaction.model.TransactionRequest;
import com.reloadly.transaction.model.TransactionResponse;
import com.reloadly.transaction.repository.AirtimeSendTxnRepository;
import com.reloadly.transaction.repository.MoneyReloadTxnRepository;
import com.reloadly.transaction.repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class TransactionServiceImpl extends TransactionProcessingSupport implements TransactionService {

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  MoneyReloadTxnRepository moneyReloadTxnRepository,
                                  AirtimeSendTxnRepository airtimeSendTxnRepository,
                                  TransactionServiceProperties properties,
                                  KafkaTemplate<String, String> template) {
        super(transactionRepository, moneyReloadTxnRepository, airtimeSendTxnRepository, properties, template);
    }

    /**
     * Accepts or rejects a transaction initiated by the caller. Note that, the transaction response is
     * returned immediately. However, the transaction is picked up by the Transaction Processor component and
     * processed as needed. Their is no guarantee that a transaction eventually succeeds, but whatever the result,
     * the caller is necessarily notified.
     * <p>
     * The caller can always query back the status of a transaction.
     *
     * @param request The transaction request object.
     * @return A transaction response object.
     * @throws ReloadlyTxnException If the transaction could not be handled.
     * @author Arun Patra
     */
    @Override
    @Transactional
    public TransactionResponse createNewTxn(TransactionRequest request) throws ReloadlyTxnException {

        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getTransactionType(), "Transaction type can not be null");
        String uid = getUid();
        Assert.notNull(uid, "UID can not be null");

        TransactionEntity te = addTransactionRecord(uid, request);
        switch (request.getTransactionType()) {
            case ADD_MONEY:
                addMoneyReloadTxnRecord(te, request);
            break;
            case SEND_AIRTIME:
                addSendAirtimeTxnRecord(te, request);
            break;
            default:
                throw new ReloadlyTxnException("Unknown transaction type");
        }

        try {
            postTransactionToKafka(te, request);
        } catch (KafkaProcessingException e) {
            throw new ReloadlyTxnException("Failed to post transaction to Kafka. Root Cause: ".concat(e.getMessage()), e);
        }

        return new TransactionResponse(te.getTxnId(), te.getTransactionStatus());
    }
}
