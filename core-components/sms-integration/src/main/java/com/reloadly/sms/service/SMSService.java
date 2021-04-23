package com.reloadly.sms.service;


import com.reloadly.sms.exception.SMSProcessingException;

public interface SMSService {

    void sendSMS(String toNumber, String message) throws SMSProcessingException;
}
