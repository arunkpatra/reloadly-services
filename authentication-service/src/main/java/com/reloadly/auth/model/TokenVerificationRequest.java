package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The JWT token verification request.
 *
 * @author Arun Patra
 */
@ApiModel(value = "TokenVerificationRequest", description = "A Reloadly issued JWT token verification request.")
public class TokenVerificationRequest {
    /**
     * A Reloadly issued JWT token to be verified.
     */
    @ApiModelProperty(value = "Token")
    private final String token;

    @JsonCreator
    public TokenVerificationRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
