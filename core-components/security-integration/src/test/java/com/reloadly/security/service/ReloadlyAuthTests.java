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

package com.reloadly.security.service;

import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import com.reloadly.security.config.ReloadlyAuthProperties;
import com.reloadly.security.exception.ReloadlyAuthException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "mock-rest-template"})
public class ReloadlyAuthTests {

    @Autowired
    private ReloadlyAuth reloadlyAuth;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ReloadlyAuthProperties properties;

    @Test
    public void should_fail_token_verification() throws Exception {
        // Setup
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        Map<String, Object> claims = new HashMap<>();
        ReloadlyAuthToken mockAuthToken = new ReloadlyAuthToken(claims);

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAuthServiceEndpoint()
                .concat("/verify/token")), any(), eq(ReloadlyAuthToken.class)))
                .thenReturn(new ResponseEntity<>(mockAuthToken, HttpStatus.UNAUTHORIZED));
        String testToken = "test-token";

        // Act and Assert
        assertThrows(ReloadlyAuthException.class, () -> reloadlyAuth.verifyToken(testToken));

        clear_mocks();

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAuthServiceEndpoint()
                .concat("/verify/token")), any(), eq(ReloadlyAuthToken.class)))
                .thenThrow(new RestClientException("Things went south"));

        // Act and Assert
        assertThrows(ReloadlyAuthException.class, () -> reloadlyAuth.verifyToken(testToken));

        clear_mocks();
    }

    @Test
    public void should_fail_api_key_verification() throws Exception {
        // Setup
        ReloadlyApiKeyIdentity mockIdentity = new ReloadlyApiKeyIdentity();
        mockIdentity.setUid("test-uid");
        mockIdentity.setRoles(Collections.singletonList("ROLE_USER"));

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAuthServiceEndpoint()
                .concat("/verify/apikey")), any(), eq(ReloadlyApiKeyIdentity.class)))
                .thenReturn(new ResponseEntity<>(mockIdentity, HttpStatus.UNAUTHORIZED));

        // Act and Assert
        String apiKey = "d3fe6f0d-120e-4161-a134-8c2342e36ca6";
        assertThrows(ReloadlyAuthException.class, () -> reloadlyAuth.verifyApiKey(apiKey, "test-client-id"));

        clear_mocks();

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAuthServiceEndpoint()
                .concat("/verify/apikey")), any(), eq(ReloadlyApiKeyIdentity.class)))
                .thenThrow(new RestClientException("Things went south"));

        // Act and Assert
        assertThrows(ReloadlyAuthException.class, () -> reloadlyAuth.verifyApiKey(apiKey, "test-client-id"));

        clear_mocks();
    }


    @Test
    public void should_verify_api_key() throws Exception {
        // Setup
        setup_mocks_for_api_key_verification();
        String apiKey = "d3fe6f0d-120e-4161-a134-8c2342e36ca6";

        // Act
        ReloadlyApiKeyIdentity apiKeyIdentity = reloadlyAuth.verifyApiKey(apiKey, "test-client-id");

        // Assert
        assertThat(apiKeyIdentity.getUid()).isEqualTo("test-uid");

        clear_mocks();
    }

    @Test
    public void should_verify_token() throws Exception {
        // Setup
        setup_mocks_for_token_verification();
        String testToken = "test-token";

        // Act
        ReloadlyAuthToken decodedToken = reloadlyAuth.verifyToken(testToken);

        // Assert
        assertThat(decodedToken.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        Mockito.clearInvocations(restTemplate);
    }

    private void setup_mocks_for_api_key_verification() throws Exception {

        ReloadlyApiKeyIdentity mockIdentity = new ReloadlyApiKeyIdentity();
        mockIdentity.setUid("test-uid");
        mockIdentity.setRoles(Collections.singletonList("ROLE_USER"));

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAuthServiceEndpoint()
                .concat("/verify/apikey")), any(), eq(ReloadlyApiKeyIdentity.class)))
                .thenReturn(new ResponseEntity<>(mockIdentity, HttpStatus.OK));
    }

    private void setup_mocks_for_token_verification() throws Exception {

        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("sub", uid);
        claims.put("iss", "Reloadly Mock Authentication Service");
        claims.put("aud", "reloadly-platform");
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        claims.put("roles", roles);
        claims.put("claims", claims);
        ReloadlyAuthToken mockAuthToken = new ReloadlyAuthToken(claims);

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAuthServiceEndpoint()
                .concat("/verify/token")), any(), eq(ReloadlyAuthToken.class)))
                .thenReturn(new ResponseEntity<>(mockAuthToken, HttpStatus.OK));
    }

    private void clear_mocks() {
        Mockito.clearInvocations(restTemplate);
    }
}
