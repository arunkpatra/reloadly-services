package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * The authentication response.
 *
 * @author Arun Patra
 */
@ApiModel(value = "AuthenticationResponse", description = "The Authentication response")
public class AuthenticationResponse implements Serializable {

    @ApiModelProperty(value = "Authentication Token", position = 1)
    private final String token;

    @ApiModelProperty(value = "Token issue timestamp", position = 2)
    private final Date tokenIssuedAt;

    @ApiModelProperty(value = "Token expiration timestamp", position = 3)
    private final Date tokenExpiresOn;

    @JsonCreator
    public AuthenticationResponse(String token, Date tokenIssuedAt, Date tokenExpiresOn) {
        this.token = token;
        this.tokenIssuedAt = tokenIssuedAt;
        this.tokenExpiresOn = tokenExpiresOn;
    }


    public String getToken() {
        return token;
    }

    public Date getTokenIssuedAt() {
        return tokenIssuedAt;
    }

    public Date getTokenExpiresOn() {
        return tokenExpiresOn;
    }

}
