package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The API key verification request.
 *
 * @author Arun Patra
 */
@ApiModel(value = "ApiKeyVerificationRequest", description = "An API Key verification request.")
public class ApiKeyVerificationRequest {
    /**
     * A Reloadly issued API Key
     */
    @ApiModelProperty(value = "API Key")
    private final String apiKey;

    @JsonCreator
    public ApiKeyVerificationRequest(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
