package com.reloadly.account.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "account_table")
public class AccountEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "uid")
    private String uid;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "currency_cd")
    private String currencyCode;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private List<AddressEntity> addressEntities;

    @OneToOne
    @JoinColumn(name = "account_balance_id", referencedColumnName = "account_balance_id")
    private AccountBalanceEntity accountBalanceEntity;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<AddressEntity> getAddressEntities() {
        return addressEntities;
    }

    public void setAddressEntities(List<AddressEntity> addressEntities) {
        this.addressEntities = addressEntities;
    }

    public AccountBalanceEntity getAccountBalanceEntity() {
        return accountBalanceEntity;
    }

    public void setAccountBalanceEntity(AccountBalanceEntity accountBalanceEntity) {
        this.accountBalanceEntity = accountBalanceEntity;
    }
}
