package com.reloadly.account.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "account_balance_table")
public class AccountBalanceEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "account_bal_uid")
    private String acctBalanceUid;

    @Column(name = "account_balance")
    private Float accountBalance;

    public String getAcctBalanceUid() {
        return acctBalanceUid;
    }

    public void setAcctBalanceUid(String acctBalanceUid) {
        this.acctBalanceUid = acctBalanceUid;
    }

    public Float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Float accountBalance) {
        this.accountBalance = accountBalance;
    }
}
