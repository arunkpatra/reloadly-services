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

package com.reloadly.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.SmsRequest;
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
