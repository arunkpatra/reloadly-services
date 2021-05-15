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
import com.reloadly.auth.model.ApiKeyCreationRequest;
import com.reloadly.auth.model.ApiKeyCreationResponse;
import com.reloadly.auth.model.SignupResponse;
import com.reloadly.auth.model.UsernamePasswordSignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApikeyControllerTests extends AbstractIntegrationTest {

    @Test
    @Transactional
    @Rollback
    public void should_create_api_key() throws Exception {

        // Setup
        ApiKeyCreationRequest request =
                new ApiKeyCreationRequest("test api key description");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/apikey")
                .content(objectMapper.writeValueAsString(request))
                .header("X-Mock-UID", "c1fe6f0d-420e-4161-a134-9c2342e36c95")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        ApiKeyCreationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ApiKeyCreationResponse.class);
        assertThat(response.getApiKey()).isNotNull();
        assertThat(response.getClientId()).isEqualTo("bafa4494-40dd-4b0c-b42e-623399e70533");
        assertThat(response.getDescription()).isEqualTo("test api key description");
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_create_api_key() throws Exception {

        // Setup
        ApiKeyCreationRequest request =
                new ApiKeyCreationRequest("test api key description");

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/apikey")
                .content(objectMapper.writeValueAsString(request))
                .header("X-Mock-UID", "a1fe6f0d-420e-4161-a134-9c2342e36c95")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }
}
