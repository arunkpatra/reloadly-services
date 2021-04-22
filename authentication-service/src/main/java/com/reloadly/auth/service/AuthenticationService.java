package com.reloadly.auth.service;

import com.reloadly.auth.exception.AuthenticationFailedException;
import com.reloadly.auth.exception.UsernameNotFoundException;
import com.reloadly.auth.model.AuthenticationResponse;

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
     * @throws UsernameNotFoundException If the username does not exist.
     * @throws AuthenticationFailedException If the username does not exist.
     */
    AuthenticationResponse authenticateUsingUsernamePassword(String username, String password)
            throws UsernameNotFoundException, AuthenticationFailedException;
}
