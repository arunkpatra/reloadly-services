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
    ReloadlyApiKeyIdentity verifyApiKey(String key) throws ReloadlyAuthException;
}
