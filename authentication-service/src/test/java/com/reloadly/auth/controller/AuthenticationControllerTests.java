package com.reloadly.auth.controller;

import com.reloadly.auth.AbstractIntegrationTest;
import com.reloadly.auth.model.AuthenticationResponse;
import com.reloadly.auth.model.UsernamePasswordAuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTests extends AbstractIntegrationTest {

    @Test
    public void should_authenticate_valid_user_pass() throws Exception{

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
    public void should_not_authenticate_invalid_pass() throws Exception{
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
    public void should_not_authenticate_non_existent_user() throws Exception{
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

}
