package com.reloadly.security.service;

import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.exception.ReloadlyAuthException;

/**
 * Java API to interact with Authentication microservice.
 *
 * @author Arun Patra
 */
public interface ReloadlyAuth {

    /**
     * Verify an Auth Service issued JWT token.
     *
     * @param token The JWT token.
     * @return A decoded and verified Reloadly auth token.
     * @throws ReloadlyAuthException If an error occurs.
     */
    ReloadlyAuthToken verifyToken(String token) throws ReloadlyAuthException;

    /**
     * Verify an API key.
     *
     * @param key Am auth service issued API key.
     * @return Identity details attached to the API key.
     * @throws ReloadlyAuthException If an error occurs.
     */
    ReloadlyApiKeyIdentity  verifyApiKey(String key) throws ReloadlyAuthException;
}
