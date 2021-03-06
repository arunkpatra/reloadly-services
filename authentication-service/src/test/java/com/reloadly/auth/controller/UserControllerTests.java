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
import com.reloadly.auth.entity.UserEntity;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.model.SignupResponse;
import com.reloadly.auth.model.UsernamePasswordAuthRequest;
import com.reloadly.auth.model.UsernamePasswordSignupRequest;
import com.reloadly.auth.repository.UserRepository;
import com.reloadly.commons.model.user.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback
    public void should_signup_valid_new_user() throws Exception {

        // Setup
        UsernamePasswordSignupRequest request =
                new UsernamePasswordSignupRequest("newuser", "password");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/signup/username")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        SignupResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SignupResponse.class);
        assertThat(response.getMessage()).isEqualTo("Signup successful");
        assertThat(response.getUid()).isNotNull();

        String uid = response.getUid();

        Optional<UserEntity> ue = userRepository.findByUid(uid);
        assertThat(ue.isPresent()).isTrue();
        assertThat(ue.get().getAuthorityEntities().size()).isEqualTo(1);
        assertThat(ue.get().getAuthorityEntities().get(0).getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_signup_new_user_with_existing_username() throws Exception {

        // Setup
        UsernamePasswordSignupRequest request =
                new UsernamePasswordSignupRequest("reloadly", "password");

        // Act and Assert
        mockMvc.perform(post("/signup/username")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void should_get_user_info_via_api_key() throws Exception {
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/me")
                .header("RELOADLY-API-KEY", "test-api-key")
                .header("RELOADLY-CLIENT-ID", "bafa4494-40dd-4b0c-b42e-623399e70533"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserInfo userInfoResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserInfo.class);

        assertThat(userInfoResponse).isNotNull();
        assertThat(userInfoResponse.getUid()).isNotNull();
        assertThat(userInfoResponse.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
    }
    @Test
    public void should_get_user_info() throws Exception {

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

        String token = response.getToken();

        // Setup and Act
        mvcResult = mockMvc.perform(get("/me")
                .header("authorization", "Bearer ".concat(token)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        UserInfo userInfoResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserInfo.class);

        assertThat(userInfoResponse).isNotNull();
        assertThat(userInfoResponse.getUid()).isNotNull();
        assertThat(userInfoResponse.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        assertThat(userInfoResponse.getRoles().size()).isEqualTo(2);

        // Setup and Act
        mvcResult = mockMvc.perform(get("/me")
                .header("X-Mock-UID", "c1fe6f0d-420e-4161-a134-9c2342e36c95"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        userInfoResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserInfo.class);

        assertThat(userInfoResponse).isNotNull();
        assertThat(userInfoResponse.getUid()).isNotNull();
        assertThat(userInfoResponse.getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
    }

    @Test
    public void should_not_get_user_info() throws Exception {
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/me"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //-----

        // Setup and Act
        mvcResult = mockMvc.perform(get("/me")
                .header("authorization", "Junk ".concat("junk")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //-----

        // Setup and Act
        mvcResult = mockMvc.perform(get("/me")
                .header("authorization", "Bearer ".concat("junk")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //-----
    }
}
