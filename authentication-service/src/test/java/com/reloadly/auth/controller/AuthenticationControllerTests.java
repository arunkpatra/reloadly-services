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

package com.reloadly.auth.controller;

import com.reloadly.auth.AbstractIntegrationTest;
import com.reloadly.auth.config.AuthenticationServiceProperties;
import com.reloadly.auth.model.ApiKeyVerificationRequest;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.model.TokenVerificationRequest;
import com.reloadly.auth.model.UsernamePasswordAuthRequest;
import com.reloadly.commons.model.ReloadlyApiKeyIdentity;
import com.reloadly.commons.model.ReloadlyAuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTests extends AbstractIntegrationTest {

    @Autowired
    private AuthenticationServiceProperties properties;

    @Test
    public void should_authenticate_valid_user_pass() throws Exception {

        // Setup
        UsernamePasswordAuthRequest request =
                new UsernamePasswordAuthRequest("reloadly", "password");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AuthenticationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AuthenticationResponse.class);
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getTokenExpiresOn()).isNotNull();
        assertThat(response.getTokenIssuedAt()).isNotNull();
    }

    @Test
    public void should_validate_valid_token() throws Exception {

        // Setup
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        UsernamePasswordAuthRequest request =
                new UsernamePasswordAuthRequest("reloadly", "password");

        MvcResult mvcResult = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        AuthenticationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AuthenticationResponse.class);
        assertThat(response.getToken()).isNotNull();

        String token = response.getToken();

        // Act
        TokenVerificationRequest tvr = new TokenVerificationRequest(token);
        mvcResult = mockMvc.perform(post("/verify/token")
                .content(objectMapper.writeValueAsString(tvr))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        ReloadlyAuthToken reloadlyAuthToken =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReloadlyAuthToken.class);
        assertThat(reloadlyAuthToken.getUid()).isNotNull();
        assertThat(reloadlyAuthToken.getUid()).isEqualTo(uid);
        assertThat(reloadlyAuthToken.getClaims()).isNotNull();
        assertThat(reloadlyAuthToken.getClaims().get("uid")).isEqualTo(uid);
        assertThat(reloadlyAuthToken.getClaims().get("sub")).isEqualTo(uid);
        assertThat(reloadlyAuthToken.getClaims().get("iss")).isEqualTo(properties.getIssuer());
        assertThat(reloadlyAuthToken.getClaims().get("aud")).isEqualTo(properties.getAudience());
        assertThat(reloadlyAuthToken.getClaims().get("roles")).isNotNull();
        List<String> roles = (List<String>) reloadlyAuthToken.getClaims().get("roles");
        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles.contains("ROLE_USER")).isTrue();
        assertThat(roles.contains("ROLE_ADMIN")).isTrue();
    }

    @Test
    public void should_not_authenticate_invalid_pass() throws Exception {
        // Setup
        UsernamePasswordAuthRequest request =
                new UsernamePasswordAuthRequest("reloadly", "badpassword");

        // Act and assert
        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void should_not_authenticate_non_existent_user() throws Exception {
        // Setup
        UsernamePasswordAuthRequest request =
                new UsernamePasswordAuthRequest("unknownuser", "badpassword");

        // Act and assert
        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void should_verify_valid_key() throws Exception {

        // Setup
        ApiKeyVerificationRequest request =
                new ApiKeyVerificationRequest("d3fe6f0d-120e-4161-a134-8c2342e36ca6");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/verify/apikey")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        ReloadlyApiKeyIdentity response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReloadlyApiKeyIdentity.class);
        assertThat(response.getUid()).isNotNull();
        assertThat(response.getRoles().size()).isEqualTo(2);
    }

    @Test
    public void should_not_verify_invalid_key() throws Exception {

        // Setup
        ApiKeyVerificationRequest request =
                new ApiKeyVerificationRequest("03fe6f0d-120e-4161-a134-8c2342e36ca6");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/verify/apikey")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void should_not_verify_disabled_key() throws Exception {

        // Setup
        ApiKeyVerificationRequest request =
                new ApiKeyVerificationRequest("f9fe6f0a-120e-1234-a134-8c2342e36c72");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/verify/apikey")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
