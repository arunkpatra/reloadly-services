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

import com.reloadly.auth.AbstractIntegrationTest;
import com.reloadly.auth.exception.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

public class UserServiceImplTests extends AbstractIntegrationTest{

    @Autowired
    private UserService userService;

    @Autowired AuthenticationService authenticationService;

    @Test
    public void should_throw_invalid_password_format_exception() {

        assertThrows(InvalidPasswordFormatException.class, () ->
                userService.createUserForUsernamePassword("test-user", "x"));

    }

    @Test
    public void should_throw_api_key_not_found_exception() throws Exception {
        UserService mockUserService = Mockito.mock(UserService.class);

        doThrow(new ApiKeyNotFoundException()).when(mockUserService).getUserInfo(any());
        assertThrows(ApiKeyNotFoundException.class, () -> mockUserService.getUserInfo(new HttpHeaders()));

        Mockito.clearInvocations(mockUserService);

        doThrow(new UserNotFoundException()).when(mockUserService).getUserInfo(any());
        assertThrows(UserNotFoundException.class, () -> mockUserService.getUserInfo(new HttpHeaders()));

    }
}
