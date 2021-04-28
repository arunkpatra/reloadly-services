package com.reloadly.sms.service;

import com.reloadly.sms.config.SmsProperties;
import com.reloadly.sms.exception.SMSProcessingException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
/**
 * A Twilio specific implementation.
 *
 * @author Arun Patra
 */
public class TwilioSMSServiceImpl implements SMSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSMSServiceImpl.class);
    private final SmsProperties properties;

    public TwilioSMSServiceImpl(SmsProperties properties) {
        this.properties = properties;
    }

    /**
     * Send an SMS.
     *
     * @param toNumber The number to which SMS should be sent.
     * @param msg The message to be sent.
     * @throws SMSProcessingException If an error occurs.
     */
    @Override
    public void sendSMS(final String toNumber, String msg) throws SMSProcessingException {
        if (properties.isDryRun()) {
            LOGGER.info("The reloadly.integration.sms.twilio.dry-run property is active, Will not route SMS through " +
                    "Twilio. If you want to route SMS messages through Twilio, switch this property to `true`.");
            return;
        }
        try {
            String numberToSend = toNumber;

            if (!toNumber.startsWith("+")) {
                numberToSend = SmsProperties.DEFAULT_COUNTRY_CODE + toNumber;
            }

            Message message = Message.creator(new PhoneNumber(numberToSend), properties.getMessagingServiceId(), msg).create();

            LOGGER.info("SMS Submitted to provider, SMSID is {}", message.getSid());
        } catch (Exception e) {
            throw new SMSProcessingException(e);
        }
    }

    @PostConstruct
    public void init() {
        if (!properties.isDryRun()) {
            Twilio.init(properties.getTwilioAccountSid(), properties.getTwilioAuthToken());
        }
    }

}
