package com.reloadly.email;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

import static org.mockito.ArgumentMatchers.any;

class TestEmailAppTests extends AbstractIntegrationTest {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private SesClient sesClient;

    @Test
    void should_send_email() {
        Mockito.when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(SendEmailResponse.builder().messageId("test").build());

        SimpleMailMessage simpleMailMessage = createSimpleMailMessage();
        mailSender.send(simpleMailMessage);
    }


    private SimpleMailMessage createSimpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("info@reloadly.com");
        simpleMailMessage.setTo("test@email.com");
        simpleMailMessage.setSubject("Message subject");
        simpleMailMessage.setText("Message body");
        return simpleMailMessage;
    }

}
