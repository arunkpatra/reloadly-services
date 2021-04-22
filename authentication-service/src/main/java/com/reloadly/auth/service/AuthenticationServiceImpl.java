package com.reloadly.auth.service;

import com.reloadly.auth.config.AuthenticationServiceProperties;
import com.reloadly.auth.entity.AuthorityEntity;
import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import com.reloadly.auth.exception.AuthenticationFailedException;
import com.reloadly.auth.exception.TokenVerificationFailedException;
import com.reloadly.auth.exception.UsernameNotFoundException;
import com.reloadly.auth.jwt.JwtTokenUtil;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.repository.AuthorityRepository;
import com.reloadly.auth.repository.UsernamePasswordCredentialsRepository;
import com.reloadly.commons.model.ReloadlyAuthToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationServiceProperties properties;
    private final AuthorityRepository authorityRepository;

    public AuthenticationServiceImpl(UsernamePasswordCredentialsRepository usernamePasswordCredentialsRepository,
                                     PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil,
                                     AuthenticationServiceProperties properties,
                                     AuthorityRepository authorityRepository) {
        this.usernamePasswordCredentialsRepository = usernamePasswordCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.properties = properties;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Returns an {@link AuthenticationResponse}. If the supplied credentials match valid token is returned.
     * Otherwise throws an exception.
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

        // This is the subject
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
