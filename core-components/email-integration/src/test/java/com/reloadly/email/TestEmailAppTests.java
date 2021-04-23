package com.reloadly.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

class TestEmailAppTests extends AbstractIntegrationTest {

    @Autowired
    private MailSender mailSender;

    @Test
    void should_send_email() {
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
