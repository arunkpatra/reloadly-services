package com.reloadly.auth.controller;

import com.reloadly.auth.AbstractIntegrationTest;
import com.reloadly.auth.model.SignupResponse;
import com.reloadly.auth.model.UsernamePasswordSignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends AbstractIntegrationTest {

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

}
