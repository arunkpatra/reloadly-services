package com.reloadly.sms;

import com.reloadly.sms.service.SMSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class ReloadlySmsAppTests extends AbstractIntegrationTest{

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SMSService smsService;

    @Test
    void should_send_sms() throws Exception{
        smsService.sendSMS("+919999999999", "Hi there! Your Reloadly account has been created. " +
                "You can now send airtime to your loved ones.");
    }
}
