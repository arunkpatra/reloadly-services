package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * The request object for username/password based authentication.
 */
@ApiModel(value = "UsernamePasswordAuthRequest", description = "The request object for username/password based authentication")
public class UsernamePasswordAuthRequest implements Serializable {

    /**
     * The username.
     */
    @ApiModelProperty(value = "Username")
    private final String username;
    /**
     * The password.
     */
    @ApiModelProperty(value = "Password")
    private final String password;

    @JsonCreator
    public UsernamePasswordAuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
