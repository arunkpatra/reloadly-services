package com.reloadly.notification.controller;

import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.notification.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotificationControllerTests extends AbstractIntegrationTest {

    @Test
    public void should_send_email() throws Exception {
        String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";
        EmailRequest request = new EmailRequest("email@email.com", "Hello Subject", "Hello Body");
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/notification/email")
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        // Assert
        NotificationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NotificationResponse.class);
        assertThat(response.getMessage()).isEqualTo("Accepted");
    }

    @Test
    public void should_send_sms() throws Exception {
        String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";
        SmsRequest request = new SmsRequest("+911234567890", "Hello Subject");
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/notification/sms")
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        // Assert
        NotificationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NotificationResponse.class);
        assertThat(response.getMessage()).isEqualTo("Accepted");
    }
}
