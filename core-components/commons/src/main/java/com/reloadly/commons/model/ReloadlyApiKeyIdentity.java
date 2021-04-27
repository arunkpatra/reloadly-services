package com.reloadly.commons.model;

import java.util.List;

public class ReloadlyApiKeyIdentity {

    private String uid;
    private List<String> roles;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
