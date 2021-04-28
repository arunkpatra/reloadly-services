package com.reloadly.autoconfig.notification.service;

import com.reloadly.autoconfig.notification.config.NotificationClientProperties;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.NotificationResponse;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.commons.model.SmsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Java API to interact with the Notification microservice.
 *
 * @author Arun Patra
 */
public class ReloadlyNotificationImpl implements ReloadlyNotification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadlyNotificationImpl.class);
    private final NotificationClientProperties properties;
    private final RestTemplate restTemplate;

    public ReloadlyNotificationImpl(NotificationClientProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    /**
     * Routes an email request to the notification microservice.
     *
     * @param credentials The credentials object containing a valid Reloadly Auth Service issued JWT token or a similar
     *                    identifier which identifies the user under whose authority the API call will be made.
     * @param request     The email request object.
     * @throws NotificationException If an error occurs.
     */
    @Override
    public void sendEmail(ReloadlyCredentials credentials, EmailRequest request) throws NotificationException {
        if (properties.isSuppress()) {
            LOGGER.info("Notification delivery is currently suppressed. Enable it by turning on the reloadly.notification.enabled property.");
            return;
        }

        // Exchange
        try {
            ResponseEntity<NotificationResponse> response =
                    restTemplate.postForEntity(properties.getReloadlyNotificationServiceEndpoint().concat("/notification/email"),
                            new HttpEntity<>(request, getHeaders(credentials)),
                            NotificationResponse.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new NotificationException("Failed to route notification. Status code is: ".concat(response.getStatusCode().toString()));
            }
        } catch (RestClientException e) {
            throw new NotificationException("Call to Notification Service failed. Root Cause : " + e.getMessage(), e);
        }
    }

    /**
     * Routes a SMS request to the notification microservice.
     *
     * @param credentials The credentials object containing a valid Reloadly Auth Service issued JWT token or a similar
     *                    identifier which identifies the user under whose authority the API call will be made.
     * @param request     The SMS request object.
     * @throws NotificationException If an error occurs.
     */
    @Override
    public void sendSms(ReloadlyCredentials credentials, SmsRequest request) throws NotificationException {
        if (properties.isSuppress()) {
            LOGGER.info("Notification delivery is currently suppressed. Enable it by turning on the reloadly.notification.enabled property.");
            return;
        }
        // Exchange
        try {
            ResponseEntity<NotificationResponse> response =
                    restTemplate.postForEntity(properties.getReloadlyNotificationServiceEndpoint().concat("/notification/sms"),
                            new HttpEntity<>(request, getHeaders(credentials)),
                            NotificationResponse.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new NotificationException("Failed to route notification. Status code is: ".concat(response.getStatusCode().toString()));
            }
        } catch (RestClientException e) {
            throw new NotificationException("Call to Notification Service failed. Root Cause : " + e.getMessage(), e);
        }
    }

    private HttpHeaders getHeaders(ReloadlyCredentials credentials) {
        HttpHeaders headers = new HttpHeaders();

        switch (credentials.getType()) {
            case RELOADLY_JWT_TOKEN:
                headers.set("Authorization", "Bearer " + credentials.getCredentials());
                break;
            case API_KEY:
                headers.set("RELOADLY-API-KEY", credentials.getCredentials());
            case MOCK_UID:
                headers.set("X-Mock-UID", credentials.getCredentials());
                break;
            default:
                LOGGER.error("Invalid credential type found. {}", credentials.getType());
                break;
        }
        return headers;
    }
}
