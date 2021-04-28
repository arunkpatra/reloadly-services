package com.reloadly.commons.model;

import java.util.List;

/**
 * An API Key identity. This wraps the UID of the user to whom the API Key is issued and the roles granted.
 *
 * @author Arun Patra
 */
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
