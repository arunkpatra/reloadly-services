package com.reloadly.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Notification microservice allows sending emails and SMS messages. Endpoints are provided for callers to invoke
 * these facilities as REST API calls.
 * <p>
 * For Email, Amazon SES is used. For SMS, Twilio is used. Other email or SMS providers could be plugged in transparently
 * since the required abstractions exist to easily swap out implementations.
 *
 * @author Arun Patra
 */
@SpringBootApplication
public class ReloadlyNotificationServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ReloadlyNotificationServiceApp.class, args);
    }

}
