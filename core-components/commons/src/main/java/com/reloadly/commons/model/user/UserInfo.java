package com.reloadly.commons.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The user info.
 *
 * @author Arun Patra
 */
public class UserInfo {

    private final String uid;
    private final List<String> roles;

    @JsonCreator
    public UserInfo(@JsonProperty("uid") String uid, @JsonProperty("roles") List<String> roles) {
        this.uid = uid;
        this.roles = roles;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getRoles() {
        return roles;
    }
}
