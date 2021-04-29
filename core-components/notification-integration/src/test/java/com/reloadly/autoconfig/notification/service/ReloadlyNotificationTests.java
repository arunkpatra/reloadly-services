package com.reloadly.autoconfig.notification.service;

import com.reloadly.autoconfig.notification.AbstractIntegrationTest;
import com.reloadly.autoconfig.notification.config.NotificationClientProperties;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.commons.model.SmsRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ReloadlyNotificationTests extends AbstractIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ReloadlyNotification reloadlyNotification;

    @Autowired
    private NotificationClientProperties properties;

    @Test
    public void should_send_email() throws Exception {
        setup_mocks_for_successful_api_calls();
        reloadlyNotification.sendEmail(testCredentials(), new EmailRequest("a@b.com", "test subject", "test body"));
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    public void should_send_sms() throws Exception {
        setup_mocks_for_successful_api_calls();
        reloadlyNotification.sendSms(testCredentials(), new SmsRequest("+19999999999", "test message"));
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    public void should_not_send_email() {
        setup_mocks_for_failed_api_calls();
        assertThrows(NotificationException.class, () -> reloadlyNotification.sendEmail(testCredentials(),
                        new EmailRequest("a@b.com", "test subject", "test body")));
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    public void should_not_send_sms() {
        setup_mocks_for_failed_api_calls();
        assertThrows(NotificationException.class, () -> reloadlyNotification.sendSms(testCredentials(),
                new SmsRequest("+19999999999", "test message")));
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    public void should_not_send_email_due_to_exception() {
        setup_mocks_for_api_calls_with_exception();
        assertThrows(NotificationException.class, () -> reloadlyNotification.sendEmail(testCredentials(),
                new EmailRequest("a@b.com", "test subject", "test body")));
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    public void should_not_send_sms_due_to_exception() {
        setup_mocks_for_api_calls_with_exception();
        assertThrows(NotificationException.class, () -> reloadlyNotification.sendSms(testCredentials(),
                new SmsRequest("+19999999999", "test message")));
        Mockito.clearInvocations(restTemplate);
    }

    private void setup_mocks_for_successful_api_calls()  {
        NotificationResponse mockResponse = new NotificationResponse("Successful");
        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyNotificationServiceEndpoint()
                .concat("/notification/sms")), any(), eq(NotificationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyNotificationServiceEndpoint()
                        .concat("/notification/email")), any(), eq(NotificationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
    }

    private void setup_mocks_for_failed_api_calls()  {
        NotificationResponse mockResponse = new NotificationResponse("Successful");
        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyNotificationServiceEndpoint()
                .concat("/notification/sms")), any(), eq(NotificationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.BAD_REQUEST));
        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyNotificationServiceEndpoint()
                .concat("/notification/email")), any(), eq(NotificationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.BAD_REQUEST));
    }

    private void setup_mocks_for_api_calls_with_exception() {
        NotificationResponse mockResponse = new NotificationResponse("Successful");
        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyNotificationServiceEndpoint()
                .concat("/notification/sms")), any(), eq(NotificationResponse.class)))
                .thenThrow(new RestClientException("Bad call"));
        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyNotificationServiceEndpoint()
                .concat("/notification/email")), any(), eq(NotificationResponse.class)))
                .thenThrow(new RestClientException("Bad call"));
    }

    private ReloadlyCredentials testCredentials() {
        ReloadlyCredentials credentials = new ReloadlyCredentials();
        credentials.setType(ReloadlyCredentials.CredentialType.MOCK_UID);
        credentials.setCredentials("test-uid");
        return credentials;
    }
}
