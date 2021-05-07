package com.reloadly.auth.service;

import com.reloadly.auth.entity.AuthorityEntity;
import com.reloadly.auth.entity.UserEntity;
import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import com.reloadly.auth.exception.*;
import com.reloadly.auth.jwt.JwtTokenUtil;
import com.reloadly.auth.repository.ApiKeyRepository;
import com.reloadly.auth.repository.AuthorityRepository;
import com.reloadly.auth.repository.UserRepository;
import com.reloadly.auth.repository.UsernamePasswordCredentialsRepository;
import com.reloadly.commons.model.user.UserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides services related to management of users.
 *
 * @author Arun Patra
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ApiKeyRepository apiKeyRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository,
                           PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
                           JwtTokenUtil jwtTokenUtil, ApiKeyRepository apiKeyRepository) {
        this.userRepository = userRepository;
        this.usernamePasswordCredentialsRepository = usernamePasswordCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.apiKeyRepository = apiKeyRepository;
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
    public UserInfo getUserInfo(Map<String, String> headers) throws UserNotFoundException, UserInfoBadRequestException,
            ApiKeyNotFoundException {

        if (headers.containsKey("authorization")) {
            return getUserInfoFromAuthHeader(headers.get("authorization"));
        }

        if (headers.containsKey("RELOADLY-API-KEY")) {
            return getUserInfoFromApiKey(headers.get("RELOADLY-API-KEY"));
        }

        if (headers.containsKey("X-Mock-UID")) {
            return new UserInfo(headers.get("X-Mock-UID"), Collections.singletonList("ROLE_USER"));
        }

        throw new UserInfoBadRequestException();
    }

    private UserInfo getUserInfoFromAuthHeader(String authHeader) throws UserNotFoundException ,
            UserInfoBadRequestException{
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

    private UserInfo getUserInfoFromApiKey(String apiKey) throws ApiKeyNotFoundException {
        String uid = apiKeyRepository.findByApiKey(apiKey).orElseThrow(ApiKeyNotFoundException::new).getUid();
        return new UserInfo(uid, authorityRepository.findAllByUid(uid).stream().map(AuthorityEntity::getAuthority)
                .collect(Collectors.toList()));
    }

    private void addUserRole(UserEntity ue) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority("ROLE_USER");
        authorityEntity.setUid(ue.getUid());

        ue.getAuthorityEntities().add(authorityEntity);

        userRepository.save(ue);
    }
}
