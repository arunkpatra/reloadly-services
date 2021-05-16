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

import com.reloadly.auth.config.AuthenticationServiceProperties;
import com.reloadly.auth.entity.AuthorityEntity;
import com.reloadly.auth.entity.ClientEntity;
import com.reloadly.auth.entity.UserEntity;
import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import com.reloadly.auth.exception.ApiKeyVerificationFailedException;
import com.reloadly.auth.exception.AuthenticationFailedException;
import com.reloadly.auth.exception.TokenVerificationFailedException;
import com.reloadly.auth.exception.UsernameNotFoundException;
import com.reloadly.auth.jwt.JwtTokenUtil;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.repository.*;
import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides authentication related services.
 *
 * @author Arun Patra
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationServiceProperties properties;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public AuthenticationServiceImpl(UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository,
                                     PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil,
                                     AuthenticationServiceProperties properties,
                                     AuthorityRepository authorityRepository, ApiKeyRepository apiKeyRepository,
                                     UserRepository userRepository, ClientRepository clientRepository) {
        this.usernamePasswordCredentialsRepository = usernamePasswordCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.properties = properties;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Returns an authentication result. If the supplied credentials match, and the username exists, returns true, else false.
     *
     * @param username The username.
     * @param password The password in cleartext. Its is matched up with the encrypted password.
     * @return AuthenticationResponse If authentication succeeds, returns the token and other details.
     * @throws UsernameNotFoundException     If the username does not exist.
     * @throws AuthenticationFailedException If the username does not exist.
     */
    @Override
    public AuthenticationResponse authenticateUsingUsernamePassword(String username, String password) throws UsernameNotFoundException,
            AuthenticationFailedException {

        Assert.notNull(username, "Username can not be null");
        Assert.notNull(username, "Password can not be null");

        UsernamePasswordCredentialsEntity upce =
                usernamePasswordCredentialsRepository.findByUsername(username)
                        .orElseThrow(UsernameNotFoundException::new);

        if (!passwordEncoder.matches(password, upce.getPassword())) {
            throw new AuthenticationFailedException("Password did not match");
        }

        String uid = upce.getUid();

        // Generate token
        try {
            String token = jwtTokenUtil.generateToken(getCustomClaims(uid), uid);
            return new AuthenticationResponse(token, new Date(),
                    new Date(System.currentTimeMillis() + properties.getJwtTokenValiditySeconds() * 1000));
        } catch (Exception e) {
            throw new AuthenticationFailedException(e.getMessage());
        }
    }

    /**
     * Verifies a presented Reloadly Authentication Service issued JWT token.
     *
     * @param token A Reloadly Authentication Service issued JWT token.
     * @return A decoded and verified {@link ReloadlyAuthToken}.
     * @throws TokenVerificationFailedException If token verification fails.
     */
    @Override
    public ReloadlyAuthToken verifyToken(String token) throws TokenVerificationFailedException {
        if (!jwtTokenUtil.validateToken(token)) {
            throw new TokenVerificationFailedException("Token could not be verified");
        }
        return new ReloadlyAuthToken(jwtTokenUtil.getAllClaimsFromToken(token));
    }

    /**
     * Verifies an API key. API keys are meant to be granted to users and applications. It may be granted to service
     * accounts as well. Service accounts are needed for threads which are not user initiated.
     *
     * @param clientId The Client ID
     * @param apiKey   The API key
     * @return A translated API key. The {@link ReloadlyApiKeyIdentity} wraps a user or a service account and the roles attached
     * to it.
     * @throws ApiKeyVerificationFailedException If verification fails
     */
    @Override
    @Transactional
    public ReloadlyApiKeyIdentity verifyApiKey(String clientId, String apiKey)
            throws ApiKeyVerificationFailedException {

        ClientEntity ce = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ApiKeyVerificationFailedException("Client ID not found"));

        if (ce.getApiKeyEntities().stream()
                .noneMatch(k -> (passwordEncoder.matches(apiKey, k.getApiKey()) && k.getActive()))) {
            throw new ApiKeyVerificationFailedException("API key not found");
        }

        UserEntity ue = userRepository.findByUid(ce.getUid())
                .orElseThrow(() -> new ApiKeyVerificationFailedException("UID not found"));
        List<String> roles = ue.getAuthorityEntities().stream()
                .map(AuthorityEntity::getAuthority).collect(Collectors.toList());

        ReloadlyApiKeyIdentity ret = new ReloadlyApiKeyIdentity();
        ret.setRoles(roles);
        ret.setUid(ue.getUid());
        return ret;
    }

    private Map<String, Object> getCustomClaims(String uid) {
        Assert.notNull(uid, "UID can not be null");

        // Add custom claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", properties.getIssuer());
        claims.put("aud", properties.getAudience());
        claims.put("uid", uid);
        claims.put("roles", getRoles(uid));
        // return
        return claims;
    }

    private List<String> getRoles(String uid) {
        return authorityRepository.findAllByUid(uid).stream().map(AuthorityEntity::getAuthority).collect(Collectors.toList());
    }
}
