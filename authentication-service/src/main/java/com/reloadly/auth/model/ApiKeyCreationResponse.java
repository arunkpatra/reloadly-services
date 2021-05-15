/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The API key verification request.
 *
 * @author Arun Patra
 */
@ApiModel(value = "ApiKeyCreationResponse", description = "An API Key creation response.")
public class ApiKeyCreationResponse {
    /**
     * A Reloadly issued API Key. Unencrypted. This is the only time, this key is handed over un-encrypted to the user.
     */
    @ApiModelProperty(value = "API Key")
    private final String apiKey;

    @ApiModelProperty(value = "Client ID")
    private final String clientId;

    @ApiModelProperty(value = "API Key description")
    private final String description;

    @JsonCreator
    public ApiKeyCreationResponse(@JsonProperty("apiKey") String apiKey, @JsonProperty("clientId") String clientId,
                                  @JsonProperty("description") String description) {
        this.apiKey = apiKey;
        this.clientId = clientId;
        this.description = description;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDescription() {
        return description;
    }
}
