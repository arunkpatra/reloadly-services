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

package com.reloadly.security.filter;

import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.security.exception.ReloadlyAuthException;
import com.reloadly.security.model.ReloadlyUserDetails;
import com.reloadly.security.service.ReloadlyAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An API Key based authentication request filter.
 *
 * @author Arun Patra
 */
public class ReloadlyApiKeyAuthenticationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyApiKeyAuthenticationRequestFilter.class);

    private final ReloadlyAuth reloadlyAuth;

    public ReloadlyApiKeyAuthenticationRequestFilter(ReloadlyAuth reloadlyAuth) {
        this.reloadlyAuth = reloadlyAuth;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        verifyApiKey(request);
        filterChain.doFilter(request, response);
    }

    private void verifyApiKey(HttpServletRequest request) {
        String apiKey = getApiKey(request);
        String clientId = getClientId(request);
        try {
            if ((apiKey != null) && (clientId != null)) {
                UserDetails userDetails = getApiKeyUserDetails(apiKey, clientId);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    getCredentials(apiKey, clientId),
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (UsernameNotFoundException e) {
            LOGGER.error("Exception: {}", e.getMessage());
        }
    }

    private String getApiKey(HttpServletRequest request) {
        return request.getHeader("RELOADLY-API-KEY");
    }

    private String getClientId(HttpServletRequest request) {
        return request.getHeader("RELOADLY-CLIENT-ID");
    }

    private ReloadlyCredentials getCredentials(String apiKey, String clientId) {
        ReloadlyCredentials credentials = new ReloadlyCredentials();
        credentials.setType(ReloadlyCredentials.CredentialType.API_KEY);
        credentials.setCredentials(apiKey);
        credentials.setClientId(clientId);
        return credentials;
    }

    private UserDetails getApiKeyUserDetails(String apiKey, String clientId) {
        ReloadlyUserDetails user = null;
        try {
            if ((apiKey != null) && (clientId != null)) {
                ReloadlyApiKeyIdentity keyIdentity = reloadlyAuth.verifyApiKey(apiKey, clientId);
                user = getReloadlyUserFromDecodedApiKeyDetails(keyIdentity);
            }
        } catch (ReloadlyAuthException e) {
            LOGGER.error("Reloadly Auth Exception: {}", e.getMessage());
            throw new UsernameNotFoundException("Username not found. Root cause: " + e.getMessage(), e);
        }
        return user;
    }

    private ReloadlyUserDetails getReloadlyUserFromDecodedApiKeyDetails(ReloadlyApiKeyIdentity keyIdentity) {
        ReloadlyUserDetails user = null;
        if (keyIdentity != null) {
            user = new ReloadlyUserDetails();
            user.setUid(keyIdentity.getUid());
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", keyIdentity.getRoles());
            user.setClaims(claims);
        }
        return user;
    }

}
