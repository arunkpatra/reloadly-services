package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * The request object for username/password based signup.
 *
 * @author Arun Patra
 */
@ApiModel(value = "UsernamePasswordSignupRequest", description = "The request object for username/password based signup")
public class UsernamePasswordSignupRequest implements Serializable {

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
    public UsernamePasswordSignupRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
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
