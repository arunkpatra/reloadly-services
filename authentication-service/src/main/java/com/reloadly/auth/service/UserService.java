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
