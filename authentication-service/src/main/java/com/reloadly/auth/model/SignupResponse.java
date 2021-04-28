package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * The signup response.
 *
 * @author Arun Patra
 */
@ApiModel(value = "SignupResponse", description = "The signup response")
public class SignupResponse implements Serializable {

    @ApiModelProperty(value = "Signup response message", position = 1)
    private final String message;
    @ApiModelProperty(value = "The UID created for the user.", position = 2)
    private final String uid;

    @JsonCreator
    public SignupResponse(String message, String uid) {
        this.message = message;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public String getUid() {
        return uid;
    }
}
