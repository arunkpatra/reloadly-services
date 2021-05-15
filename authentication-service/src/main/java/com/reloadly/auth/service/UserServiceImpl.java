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
import com.reloadly.auth.entity.UserEntity;
import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import com.reloadly.auth.exception.*;
import com.reloadly.auth.jwt.JwtTokenUtil;
import com.reloadly.auth.repository.*;
import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.user.UserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides services related to management of users.
 *
 * @author Arun Patra
 */
@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationServiceProperties properties;
    private final UserRepository userRepository;
    private final UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ApiKeyRepository apiKeyRepository;
    private final ClientRepository clientRepository;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(AuthenticationServiceProperties properties,
                           UserRepository userRepository,
                           UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository,
                           PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
                           JwtTokenUtil jwtTokenUtil, ApiKeyRepository apiKeyRepository,
                           ClientRepository clientRepository,
                           AuthenticationService authenticationService) {
        this.properties = properties;
        this.userRepository = userRepository;
        this.usernamePasswordCredentialsRepository = usernamePasswordCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.apiKeyRepository = apiKeyRepository;
        this.clientRepository = clientRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Create a new user.
     *
     * @param username The username
     * @param password The password. It will be stored in an encrypted format.
     * @return The UID. Its a unique identifier across the authentication system.
     * @throws UsernameAlreadyTakenException  If the username exists already.
     * @throws InvalidPasswordFormatException If the password format is not valid. A rudimentary 8 character
     *                                        check is enforced.
     */
    @Override
    @Transactional
    public String createUserForUsernamePassword(String username, String password) throws UsernameAlreadyTakenException,
            InvalidPasswordFormatException {

        Assert.notNull(username, "Username can not be null");
        Assert.notNull(password, "Password can not be null");

        if (password.length() < 8) {
            throw new InvalidPasswordFormatException();
        }

        UserEntity ue;
        // Create a user first
        try {
            ue = new UserEntity();
            ue.setUid(UUID.randomUUID().toString());
            ue.setActive(true);
            ue = userRepository.save(ue);
        } catch (Exception e) {
            throw new UsernameAlreadyTakenException();
        }

        // Add a ROLE_USER role.
        addUserRole(ue);

        // Now create username password(encrypted) record
        try {
            Assert.notNull(ue, "UserEntity can not be null");
            Assert.notNull(ue.getUid(), "UID can not be null");
            String encodedPassword = passwordEncoder.encode(password);
            UsernamePasswordCredentialsEntity upce = new UsernamePasswordCredentialsEntity();
            upce.setUid(ue.getUid());
            upce.setUsername(username);
            upce.setPassword(encodedPassword);

            usernamePasswordCredentialsRepository.save(upce);
        } catch (Exception e) {
            throw new UsernameAlreadyTakenException(String.format("Failed to create UID. Original exception is %s",
                    e.getMessage()), e);
        }
        return ue.getUid();
    }

    /**
     * Retrieves user info details from user presented authentication data.
     *
     * @param headers The HTTP request headers.
     * @return The {@link UserInfo} object.
     * @throws UserNotFoundException       If the user could not be located.
     * @throws UserInfoBadRequestException If required headers were not found.
     */
    @Override
    @Transactional
    public UserInfo getUserInfo(HttpHeaders headers) throws UserNotFoundException, UserInfoBadRequestException,
            ApiKeyNotFoundException {

        if (headers.containsKey("authorization")) {
            return getUserInfoFromAuthHeader(Objects.requireNonNull(headers.get("authorization")).get(0));
        }

        if (headers.containsKey("RELOADLY-API-KEY") && headers.containsKey("RELOADLY-CLIENT-ID")) {
            return getUserInfoFromApiKey(Objects.requireNonNull(headers.get("RELOADLY-API-KEY")).get(0),
                    Objects.requireNonNull(headers.get("RELOADLY-CLIENT-ID")).get(0));
        }

        if (properties.isMockEnabled() && headers.containsKey("X-Mock-UID")) {
            UserEntity ue = userRepository.findByUid((headers.get("X-Mock-UID")).get(0)).orElseThrow(UserInfoBadRequestException::new);
            return new UserInfo(ue.getUid(),
                    ue.getAuthorityEntities().stream().map(AuthorityEntity::getAuthority).collect(Collectors.toList()));
        }

        throw new UserInfoBadRequestException();
    }

    private UserInfo getUserInfoFromAuthHeader(String authHeader) throws UserNotFoundException,
            UserInfoBadRequestException {
        // See if Bearer token exists
        if (!authHeader.startsWith("Bearer ")) {
            throw new UserInfoBadRequestException();
        }
        String token = authHeader.substring(7);
        boolean validationSuccessful = jwtTokenUtil.validateToken(token);
        if (!validationSuccessful) {
            throw new UserInfoBadRequestException();
        }
        String uid = userRepository.findByUid(jwtTokenUtil.getSubjectFromToken(token))
                .orElseThrow(UserNotFoundException::new).getUid();

        return new UserInfo(uid,
                authorityRepository.findAllByUid(uid).stream().map(AuthorityEntity::getAuthority)
                        .collect(Collectors.toList()));
    }

    private UserInfo getUserInfoFromApiKey(String apiKey, String clientId) throws ApiKeyNotFoundException {
        try {
            ReloadlyApiKeyIdentity apiKeyIdentity = authenticationService.verifyApiKey(clientId, apiKey);
            String uid = apiKeyIdentity.getUid();
            return new UserInfo(uid, authorityRepository.findAllByUid(uid).stream().map(AuthorityEntity::getAuthority)
                    .collect(Collectors.toList()));
        } catch (ApiKeyVerificationFailedException e) {
            throw new ApiKeyNotFoundException();
        }
    }

    private void addUserRole(UserEntity ue) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority("ROLE_USER");
        authorityEntity.setUid(ue.getUid());

        ue.getAuthorityEntities().add(authorityEntity);

        userRepository.save(ue);
    }
}
