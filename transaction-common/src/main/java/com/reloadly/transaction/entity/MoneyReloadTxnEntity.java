package com.reloadly.transaction.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Money reload JPA entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "money_reload_txn_table")
public class MoneyReloadTxnEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "amount")
    private Float amount;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
