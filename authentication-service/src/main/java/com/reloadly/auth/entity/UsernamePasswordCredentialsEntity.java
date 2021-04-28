package com.reloadly.auth.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The UsernamePassword entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "username_password_table")
public class UsernamePasswordCredentialsEntity extends AbstractPersistable<Long> {

    @Column(name = "uid")
    private String uid;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
