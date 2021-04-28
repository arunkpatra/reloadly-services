package com.reloadly.notification.service;

import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.email.config.SesProperties;
import com.reloadly.sms.exception.SMSProcessingException;
import com.reloadly.sms.service.SMSService;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Notification service to send emails and SMS.
 *
 * @author Arun Patra
 */
@Service
public class NotificationServiceImpl implements NotificationService{

    private final SMSService smsService;
    private final MailSender mailSender;
    private final SesProperties sesProperties;

    public NotificationServiceImpl(SMSService smsService, MailSender mailSender, SesProperties sesProperties) {
        this.smsService = smsService;
        this.mailSender = mailSender;
        this.sesProperties = sesProperties;
    }

    /**
     * Send an email.
     *
     * @param request The email request.
     * @throws NotificationException If an exception occurs.
     */
    @Override
    public void sendEmail(EmailRequest request) throws NotificationException {
        try {
            SimpleMailMessage simpleMailMessage = createSimpleMailMessage(request);
            mailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new NotificationException("Email sending failed. Root cause: ".concat(e.getMessage()), e);
        }
    }

    /**
     * Send an SMS.
     *
     * @param request The SMS request.
     * @throws NotificationException If an exception occurs.
     */
    @Override
    public void sendSms(SmsRequest request) throws NotificationException {
        try {
            smsService.sendSMS(request.getPhoneNumber(), request.getMessage());
        } catch (SMSProcessingException e) {
            throw new NotificationException("SMS sending failed. Root cause: ".concat(e.getMessage()), e);
        }
    }

    private SimpleMailMessage createSimpleMailMessage(EmailRequest request) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sesProperties.getFromEmailId());
        simpleMailMessage.setTo(request.getEmail());
        simpleMailMessage.setSubject(request.getSubject());
        simpleMailMessage.setText(request.getBody());
        return simpleMailMessage;
    }
}
