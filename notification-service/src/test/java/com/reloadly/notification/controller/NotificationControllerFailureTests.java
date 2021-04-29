package com.reloadly.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.exceptions.ReloadlyException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.notification.AbstractIntegrationTest;
import com.reloadly.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "mocked-notification-svc"})
public class NotificationControllerFailureTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void should_not_send_email() throws Exception {
        doThrow(NotificationException.class).when(notificationService).sendEmail(any());

        String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";
        EmailRequest request = new EmailRequest("email@email.com", "Hello Subject", "Hello Body");
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/notification/email")
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        NotificationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NotificationResponse.class);
        assertThat(response.getMessage()).isEqualTo("Rejected");

        Mockito.clearInvocations(notificationService);
    }

    @Test
    public void should_not_send_sms() throws Exception {
        doThrow(NotificationException.class).when(notificationService).sendSms(any());

        String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";
        SmsRequest request = new SmsRequest("+911234567890", "Hello Subject");
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/notification/sms")
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        NotificationResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NotificationResponse.class);
        assertThat(response.getMessage()).isEqualTo("Rejected");

        Mockito.clearInvocations(notificationService);
    }
}
