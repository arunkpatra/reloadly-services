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

package com.reloadly.auth.service;

import com.reloadly.auth.exception.ApiKeyVerificationFailedException;
import com.reloadly.auth.exception.AuthenticationFailedException;
import com.reloadly.auth.exception.TokenVerificationFailedException;
import com.reloadly.auth.exception.UsernameNotFoundException;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;

/**
 * Provides authentication related services.
 *
 * @author Arun Patra
 */
public interface AuthenticationService {

    /**
     * Returns an authentication result. If the supplied credentials match, and the username exists, returns true, else false.
     *
     * @param username The username.
     * @param password The password in cleartext. Its is matched up with the encrypted password.
     * @return AuthenticationResponse If authentication succeeds, returns the token and other details.
     * @throws UsernameNotFoundException     If the username does not exist.
     * @throws AuthenticationFailedException If the username does not exist.
     */
    AuthenticationResponse authenticateUsingUsernamePassword(String username, String password)
            throws UsernameNotFoundException, AuthenticationFailedException;

    /**
     * Verifies a presented Reloadly Authentication Service issued JWT token.
     *
     * @param token A Reloadly Authentication Service issued JWT token.
     * @return A decoded and verified {@link ReloadlyAuthToken}.
     * @throws TokenVerificationFailedException If token verification fails.
     */
    ReloadlyAuthToken verifyToken(String token) throws TokenVerificationFailedException;

    /**
     * Verifies an API key. API keys are meant to be granted to users and applications. It may be granted to service
     * accounts as well. Service accounts are needed for threads which are not user initiated.
     *
     * @param apiKey The API key
     * @return A translated API key. The {@link ReloadlyApiKeyIdentity} wraps a user or a service account and the roles attached
     * to it.
     * @throws ApiKeyVerificationFailedException If verification fails
     */
    ReloadlyApiKeyIdentity verifyApiKey(String apiKey) throws ApiKeyVerificationFailedException;
}
