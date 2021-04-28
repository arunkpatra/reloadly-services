package com.reloadly.account.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The account balance JPA entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "account_balance_table")
public class AccountBalanceEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "account_balance")
    private Float accountBalance;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Float accountBalance) {
        this.accountBalance = accountBalance;
    }
}
