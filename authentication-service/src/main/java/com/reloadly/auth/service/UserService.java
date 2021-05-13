package com.reloadly.auth.service;

import com.reloadly.auth.exception.*;
import com.reloadly.commons.model.user.UserInfo;
import org.springframework.http.HttpHeaders;

/**
 * Provides services related to management of users.
 *
 * @author Arun Patra
 */
public interface UserService {

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
    String createUserForUsernamePassword(String username, String password) throws UsernameAlreadyTakenException,
            InvalidPasswordFormatException;

    /**
     * Retrieves user info details from user presented authentication data.
     *
     * @param headers The HTTP request headers.
     * @return The {@link UserInfo} object.
     * @throws UserNotFoundException       If the user could not be located.
     * @throws UserInfoBadRequestException If required headers were not found.
     */
    UserInfo getUserInfo(HttpHeaders headers) throws UserNotFoundException, UserInfoBadRequestException, ApiKeyNotFoundException;
}
