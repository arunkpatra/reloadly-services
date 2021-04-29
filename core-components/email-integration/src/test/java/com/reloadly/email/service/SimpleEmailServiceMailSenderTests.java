package com.reloadly.email.service;

import com.reloadly.email.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import java.util.Date;

public class SimpleEmailServiceMailSenderTests extends AbstractIntegrationTest {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private SesClient sesClient;

    @Test
    public void should_send_email() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("a@b.com");
        message.setBcc("a@b.com");
        message.setCc("a@b.com");
        message.setReplyTo("a@b.com");
        message.setSubject("test subject");
        message.setText("Hello there!");
        message.setTo(new String[]{"a@b.com, c@d.com"});
        message.setSentDate(new Date());

        Mockito.when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(SendEmailResponse.builder().messageId("test").build());

        mailSender.send(message);
    }


    private SendEmailRequest prepareMessage(SimpleMailMessage simpleMailMessage) {
        Destination.Builder destinationBuilder = Destination.builder();
        destinationBuilder.toAddresses(simpleMailMessage.getTo());

        if (simpleMailMessage.getCc() != null) {
            destinationBuilder.ccAddresses(simpleMailMessage.getCc());
        }

        if (simpleMailMessage.getBcc() != null) {
            destinationBuilder.bccAddresses(simpleMailMessage.getBcc());
        }

        Content subject = Content.builder().data(simpleMailMessage.getSubject()).build();
        Body body = Body.builder().text(Content.builder().data(simpleMailMessage.getText()).build()).build();
        Message message = Message.builder().body(body).subject(subject).build();

        SendEmailRequest.Builder emailRequestBuilder = SendEmailRequest.builder()
                .destination(destinationBuilder.build()).source(simpleMailMessage.getFrom()).message(message);

        // .content(EmailContent.builder().simple(message).build());

        if (StringUtils.hasText(simpleMailMessage.getReplyTo())) {
            emailRequestBuilder.replyToAddresses(simpleMailMessage.getReplyTo());
        }

        return emailRequestBuilder.build();
    }
}
