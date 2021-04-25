package com.reloadly.transaction.entity;

import com.reloadly.transaction.model.TransactionStatus;
import com.reloadly.transaction.model.TransactionType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "txn_id", referencedColumnName = "txn_id")
    private List<MoneyReloadTxnEntity> moneyReloadTxnEntities;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "txn_id", referencedColumnName = "txn_id")
    private List<AirtimeSendTxnEntity> airtimeSendTxnEntities;

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

    public List<MoneyReloadTxnEntity> getMoneyReloadTxnEntities() {
        return moneyReloadTxnEntities;
    }

    public void setMoneyReloadTxnEntities(List<MoneyReloadTxnEntity> moneyReloadTxnEntities) {
        this.moneyReloadTxnEntities = moneyReloadTxnEntities;
    }

    public List<AirtimeSendTxnEntity> getAirtimeSendTxnEntities() {
        return airtimeSendTxnEntities;
    }

    public void setAirtimeSendTxnEntities(List<AirtimeSendTxnEntity> airtimeSendTxnEntities) {
        this.airtimeSendTxnEntities = airtimeSendTxnEntities;
    }
}
