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
import com.reloadly.auth.exception.ApiKeyNotFoundException;
import com.reloadly.auth.exception.TokenVerificationFailedException;
import com.reloadly.auth.exception.UsernameNotFoundException;
import com.reloadly.auth.jwt.JwtTokenUtil;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

public class AuthenticationServiceImplTests extends AbstractIntegrationTest {


    @Test
    public void should_throw_TokenVerificationFailedException() throws Exception {
        AuthenticationService mockAuthenticationService = Mockito.mock(AuthenticationService.class);

        doThrow(new TokenVerificationFailedException("Failed")).when(mockAuthenticationService).verifyToken(any());

        assertThrows(TokenVerificationFailedException.class, () ->
                mockAuthenticationService.verifyToken(eq("test-token")));
    }

    @Test
    public void should_throw_UsernameNotFoundException() throws Exception {
        AuthenticationService mockAuthenticationService = Mockito.mock(AuthenticationService.class);
        doThrow(new UsernameNotFoundException()).when(mockAuthenticationService).authenticateUsingUsernamePassword(any(), any());

        assertThrows(UsernameNotFoundException.class, () ->
                mockAuthenticationService.authenticateUsingUsernamePassword("user", "password"));
    }
}
