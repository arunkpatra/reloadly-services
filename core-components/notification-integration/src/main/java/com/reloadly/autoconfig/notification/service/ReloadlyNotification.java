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

package com.reloadly.autoconfig.notification.service;

import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.EmailRequest;
import com.reloadly.commons.model.ReloadlyCredentials;
import com.reloadly.commons.model.SmsRequest;

/**
 * Java API to interact with the Notification microservice.
 *
 * @author Arun Patra
 */
public interface ReloadlyNotification {

    /**
     * Routes an email request to the notification microservice.
     *
     * @param credentials The credentials object containing a valid Reloadly Auth Service issued JWT token or a similar
     *                    identifier which identifies the user under whose authority the API call will be made.
     * @param request     The email request object.
     * @throws NotificationException If an error occurs.
     */
    void sendEmail(ReloadlyCredentials credentials, EmailRequest request) throws NotificationException;

    /**
     * Routes a SMS request to the notification microservice.
     *
     * @param credentials The credentials object containing a valid Reloadly Auth Service issued JWT token or a similar
     *                    identifier which identifies the user under whose authority the API call will be made.
     * @param request     The SMS request object.
     * @throws NotificationException If an error occurs.
     */
    void sendSms(ReloadlyCredentials credentials, SmsRequest request) throws NotificationException;
}
