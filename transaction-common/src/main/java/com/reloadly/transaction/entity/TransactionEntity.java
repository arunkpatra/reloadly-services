package com.reloadly.transaction.entity;

import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.model.TransactionType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Transaction JPA entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "transaction_table")
public class TransactionEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "uid")
    private String uid;

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "txn_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "txn_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
