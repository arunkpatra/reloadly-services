package com.reloadly.email.service;

import com.reloadly.email.config.SesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple MailSender implementation to send E-Mails with the Amazon Simple Email Service.
 * This implementation has no dependencies to the Java Mail API. It can be used to send
 * simple mail messages that doesn't have any attachment and therefore only consist of a
 * text body and a subject line.
 *
 * @author Arun Patra
 */
public class SimpleEmailServiceMailSender implements MailSender, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEmailServiceMailSender.class);
    private static final String DRY_RUN_INFO_MESSAGE = "This is a dry run since the reloadly.integration.email.ses.dry-run property is active. Disable this property to route emails through Amazon SES.";

    private final SesClient sesClient;
    private final SesProperties properties;

    public SimpleEmailServiceMailSender(SesClient sesClient, SesProperties properties) {
        this.sesClient = sesClient;
        this.properties = properties;
    }

    @Override
    public void destroy() {
        sesClient.close();
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        if (properties.isDryRun()) {
            LOGGER.info(DRY_RUN_INFO_MESSAGE);
            return;
        }
        send(new SimpleMailMessage[]{simpleMessage});
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        if (properties.isDryRun()) {
            LOGGER.info(DRY_RUN_INFO_MESSAGE);
            return;
        }

        Map<Object, Exception> failedMessages = new HashMap<>();

        for (SimpleMailMessage simpleMessage : simpleMessages) {
            try {
                SendEmailResponse sendEmailResult = getEmailService().sendEmail(prepareMessage(simpleMessage));
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Message with id: {} successfully send", sendEmailResult.messageId());
                }
            } catch (SesException e) {
                // Ignore Exception because we are collecting and throwing all if any
                // noinspection ThrowableResultOfMethodCallIgnored
                failedMessages.put(simpleMessage, e);
            }
        }

        if (!failedMessages.isEmpty()) {
            throw new MailSendException(failedMessages);
        }

    }

    protected SesClient getEmailService() {
        return this.sesClient;
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
