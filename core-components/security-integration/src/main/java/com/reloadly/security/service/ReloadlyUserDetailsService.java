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

import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.exception.ReloadlyAuthException;
import com.reloadly.security.model.ReloadlyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * User detail service for the auth service.
 *
 * @author Arun Patra
 */
public class ReloadlyUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyUserDetailsService.class);

    private final ReloadlyAuth reloadlyAuth;

    public ReloadlyUserDetailsService(ReloadlyAuth reloadlyAuth) {
        this.reloadlyAuth = reloadlyAuth;
    }

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        ReloadlyUserDetails user = null;
        try {
            if (token != null) {
                ReloadlyAuthToken decodedToken = reloadlyAuth.verifyToken(token);
                user = getReloadlyUserFromDecodedToken(decodedToken);
            }
        } catch (ReloadlyAuthException e) {
            LOGGER.error("Reloadly Auth Exception: {}", e.getMessage());
            throw new UsernameNotFoundException("Username not found. Root cause: " + e.getMessage(), e);
        }
        return user;
    }

    private ReloadlyUserDetails getReloadlyUserFromDecodedToken(ReloadlyAuthToken decodedToken) {
        ReloadlyUserDetails user = null;
        if (decodedToken != null) {
            user = new ReloadlyUserDetails();
            user.setUid((String) decodedToken.getClaims().get("uid"));
            user.setIssuer((String) decodedToken.getClaims().get("issuer"));
            user.setAudience((String) decodedToken.getClaims().get("audience"));
            user.setClaims((Map<String, Object>) decodedToken.getClaims().get("claims"));
        }
        return user;
    }
}
