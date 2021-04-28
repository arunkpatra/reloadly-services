package com.reloadly.auth.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The User Authority JPA entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "authority_table")
public class AuthorityEntity extends AbstractPersistable<Long> {

    @Column(name = "uid")
    private String uid;

    @Column(name = "authority")
    private String authority;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
