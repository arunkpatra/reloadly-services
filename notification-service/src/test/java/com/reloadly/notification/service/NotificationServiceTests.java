package com.reloadly.notification.service;

import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.SmsRequest;
import com.reloadly.sms.exception.SMSProcessingException;
import com.reloadly.sms.service.SMSService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles({"test", "mocked-mail-sender", "mocked-sms-service"})
public class NotificationServiceTests {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private SMSService smsService;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void should_not_sendEmail() {
        doThrow(new MailException("") {}).when(mailSender).send(any(SimpleMailMessage.class));
        assertThrows(NotificationException.class, () ->
                notificationService.sendEmail(new EmailRequest("a@b.com", "test", "test")));
        Mockito.clearInvocations(mailSender);
    }

    @Test
    public void should_not_send_sms() throws Exception {
        doThrow(SMSProcessingException.class).when(smsService).sendSMS(any(), any());
        assertThrows(NotificationException.class, () ->
                notificationService.sendSms(new SmsRequest("test", "test")));
        Mockito.clearInvocations(smsService);
    }

}
