package com.reloadly.auth.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class UserEntity extends AbstractPersistable<Long> implements Serializable {

    @Column(name = "active")
    private Boolean active;

    @Column(name = "uid")
    private String uid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private List<AuthorityEntity> authorityEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private List<ApiKeyEntity> apiKeyEntities = new ArrayList<>();

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<AuthorityEntity> getAuthorityEntities() {
        return authorityEntities;
    }

    public void setAuthorityEntities(List<AuthorityEntity> authorityEntities) {
        this.authorityEntities = authorityEntities;
    }

    public List<ApiKeyEntity> getApiKeyEntities() {
        return apiKeyEntities;
    }

    public void setApiKeyEntities(List<ApiKeyEntity> apiKeyEntities) {
        this.apiKeyEntities = apiKeyEntities;
    }
}
