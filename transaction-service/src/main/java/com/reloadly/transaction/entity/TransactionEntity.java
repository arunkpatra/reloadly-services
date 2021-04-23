package com.reloadly.transaction.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "transaction_table")
public class TransactionEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "txn_id")
    private String txnId;

}
