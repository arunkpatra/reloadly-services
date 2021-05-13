package com.reloadly.sms;

import com.reloadly.sms.config.SmsProperties;
import com.reloadly.sms.service.SMSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class ReloadlySmsAppTests extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SMSService smsService;

    @Autowired
    private SmsProperties smsProperties;

    @Test
    void should_send_sms() throws Exception {
        smsService.sendSMS("+919999999999", "Hi there! Your Reloadly account has been created. " +
                "You can now send airtime to your loved ones.");
        assertThat(smsProperties.getMessagingServiceId()).isEqualTo("XXX");
        assertThat(smsProperties.getTwilioAccountSid()).isEqualTo("YYY");
        assertThat(smsProperties.getTwilioAuthToken()).isEqualTo("ZZZ");

    }
}
