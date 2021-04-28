package com.reloadly.transaction.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Airtime send JPA entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "airtime_send_txn_table")
public class AirtimeSendTxnEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "txn_id")
    private String txnId;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "phone_number")
    private String phoneNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
