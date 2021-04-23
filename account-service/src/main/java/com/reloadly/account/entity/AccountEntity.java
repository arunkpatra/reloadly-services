package com.reloadly.account.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account_table")
public class AccountEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "active")
    private Boolean active;

    @Column(name = "account_id")
    private String accountId;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
