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

package com.reloadly.security.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.config.ReloadlyAuthProperties;
import com.reloadly.security.exception.ReloadlyAuthException;
import com.reloadly.tracing.utils.TracingUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ReloadlyAuthServiceImpl implements ReloadlyAuth {

    private final ReloadlyAuthProperties properties;
    private final RestTemplate restTemplate;
    private final ApplicationContext context;

    public ReloadlyAuthServiceImpl(ReloadlyAuthProperties properties, RestTemplate restTemplate, ApplicationContext context) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.context = context;
    }

    /**
     * Verify an Auth Service issued JWT token.
     *
     * @param token The JWT token.
     * @return A decoded and verified Reloadly auth token.
     * @throws ReloadlyAuthException If an error occurs.
     */
    @Override
    public ReloadlyAuthToken verifyToken(String token) throws ReloadlyAuthException {
        Assert.hasLength(properties.getReloadlyAuthServiceEndpoint(), "Authentication endpoint URL can not be empty.");

        // Exchange
        try {
            ResponseEntity<ReloadlyAuthToken> response =
                    restTemplate.postForEntity(properties.getReloadlyAuthServiceEndpoint().concat("/verify/token"),
                            new HttpEntity<>(new AuthRequest(token),
                                    TracingUtils.getPropagatedHttpHeaders(new HttpHeaders(), context)),
                            ReloadlyAuthToken.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new ReloadlyAuthException("Token verification failed.");
            }
        } catch (RestClientException e) {
            throw new ReloadlyAuthException("Token verification failed. Root cause: " + e.getMessage(), e);
        }
    }

    /**
     * Verify an API key.
     *
     * @param key Am auth service issued API key.
     * @return Identity details attached to the API key.
     * @throws ReloadlyAuthException If an error occurs.
     */
    @Override
    public ReloadlyApiKeyIdentity verifyApiKey(String key) throws ReloadlyAuthException {
        Assert.hasLength(properties.getReloadlyAuthServiceEndpoint(), "Authentication endpoint URL can not be empty.");

        // Exchange
        try {
            ResponseEntity<ReloadlyApiKeyIdentity> response =
                    restTemplate.postForEntity(properties.getReloadlyAuthServiceEndpoint().concat("/verify/apikey"),
                            new HttpEntity<>(new ApiKeyRequest(key),
                                    TracingUtils.getPropagatedHttpHeaders(new HttpHeaders(), context)),
                            ReloadlyApiKeyIdentity.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new ReloadlyAuthException("API Key verification failed.");
            }
        } catch (RestClientException e) {
            throw new ReloadlyAuthException("API Key verification failed. Root cause: " + e.getMessage(), e);
        }
    }

    static class ApiKeyRequest {
        final String apiKey;

        @JsonCreator
        public ApiKeyRequest(@JsonProperty("apiKey") String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiKey() {
            return apiKey;
        }
    }

    static class AuthRequest {
        final String token;

        @JsonCreator
        public AuthRequest(@JsonProperty("token") String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
