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

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

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
