package com.reloadly.auth.service;

import com.reloadly.auth.config.AuthenticationServiceProperties;
import com.reloadly.auth.entity.ApiKeyEntity;
import com.reloadly.auth.entity.AuthorityEntity;
import com.reloadly.auth.entity.UserEntity;
import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import com.reloadly.auth.exception.ApiKeyVerificationFailedException;
import com.reloadly.auth.exception.AuthenticationFailedException;
import com.reloadly.auth.exception.TokenVerificationFailedException;
import com.reloadly.auth.exception.UsernameNotFoundException;
import com.reloadly.auth.jwt.JwtTokenUtil;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.repository.ApiKeyRepository;
import com.reloadly.auth.repository.AuthorityRepository;
import com.reloadly.auth.repository.UserRepository;
import com.reloadly.auth.repository.UsernamePasswordCredentialsRepository;
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
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository,
                                     PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil,
                                     AuthenticationServiceProperties properties,
                                     AuthorityRepository authorityRepository, ApiKeyRepository apiKeyRepository, UserRepository userRepository) {
        this.usernamePasswordCredentialsRepository = usernamePasswordCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.properties = properties;
        this.authorityRepository = authorityRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.userRepository = userRepository;
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

        Optional<UsernamePasswordCredentialsEntity> upceOptional =
                usernamePasswordCredentialsRepository.findByUsername(username);

        if (!upceOptional.isPresent()) {
            throw new UsernameNotFoundException();
        }

        if (!passwordEncoder.matches(password, upceOptional.get().getPassword())) {
            throw new AuthenticationFailedException();
        }

        String uid = upceOptional.get().getUid();

        // Generate token
        try {
            String token = jwtTokenUtil.generateToken(getCustomClaims(uid), uid);
            return new AuthenticationResponse(token, new Date(),
                    new Date(System.currentTimeMillis() + properties.getJwtTokenValiditySeconds() * 1000));
        } catch (Exception e) {
            throw new AuthenticationFailedException(e.getMessage(), e);
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
            throw new TokenVerificationFailedException();
        }
        return new ReloadlyAuthToken(jwtTokenUtil.getAllClaimsFromToken(token));
    }

    /**
     * Verifies an API key. API keys are meant to be granted to users and applications. It may be granted to service
     * accounts as well. Service accounts are needed for threads which are not user initiated.
     *
     * @param apiKey The API key
     * @return A translated API key. The {@link ReloadlyApiKeyIdentity} wraps a user or a service account and the roles attached
     * to it.
     * @throws ApiKeyVerificationFailedException If verification fails
     */
    @Override
    @Transactional
    public ReloadlyApiKeyIdentity verifyApiKey(String apiKey) throws ApiKeyVerificationFailedException {
        Optional<ApiKeyEntity> akeOpt = apiKeyRepository.findByApiKey(apiKey);
        if (!akeOpt.isPresent()) {
            throw new ApiKeyVerificationFailedException("API key not found");
        }

        if (!akeOpt.get().getActive()) {
            throw new ApiKeyVerificationFailedException("API key is disabled");
        }

        UserEntity ue = userRepository.findByUid(akeOpt.get().getUid()).get();
        List<String> roles = ue.getAuthorityEntities().stream().map(AuthorityEntity::getAuthority).collect(Collectors.toList());

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
