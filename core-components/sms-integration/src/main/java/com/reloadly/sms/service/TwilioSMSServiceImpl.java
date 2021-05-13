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
     * @param msg      The message to be sent.
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
