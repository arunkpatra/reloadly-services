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

package com.reloadly.transaction.service;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.exceptions.NotificationException;
import com.reloadly.commons.model.account.AccountCreditResp;
import com.reloadly.commons.model.account.AccountDebitResp;
import com.reloadly.commons.model.account.AccountInfo;
import com.reloadly.transaction.AbstractIntegrationTest;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import com.reloadly.transaction.listener.TransactionListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

public class TransactionProcessorServiceTests extends AbstractIntegrationTest {

    @Autowired
    protected TransactionProcessorProperties properties;

    @Autowired
    private TransactionProcessorService tps;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ReloadlyNotification notification;

    @Autowired
    private KafkaTemplate<String, String> template;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TransactionListener transactionListener;

    @Test
    @Transactional
    @Rollback
    public void should_process_money_load_txn() throws Exception {
        // Setup
        String moneyReloadTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        setup_mocks_for_should_process_money_load_txn();

        // Execute
        transactionListener.listen(moneyReloadTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_process_money_load_txn() throws Exception {
        // Setup
        String moneyReloadTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        setup_mocks_for_should_not_process_money_load_txn();

        // Execute
        transactionListener.listen(moneyReloadTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_process_money_load_txn_invalid_txn_id() throws Exception {
        // Setup
        String moneyReloadTxnId = "e8fe6f0d-420e-5161-a134-9c2342e36c32";
        setup_mocks_for_should_not_process_money_load_txn();

        // Execute
        transactionListener.listen(moneyReloadTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_process_money_load_txn_due_to_rest_client_error() throws Exception {
        // Setup
        String moneyReloadTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";

        setup_mocks_for_should_not_process_money_load_txn_due_to_rest_client_error();

        // Execute
        transactionListener.listen(moneyReloadTxnId, getHeaders());
        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_process_airtime_send_txn() throws Exception {
        //Setup
        String sendAirtimeTxnId = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        setup_mocks_for_should_process_airtime_send_txn();

        // Act
        transactionListener.listen(sendAirtimeTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_process_airtime_send_txn_invalid_txn_id() throws Exception {
        //Setup
        String sendAirtimeTxnId = "b8fe6f0d-420e-5121-a134-9c2342e36c64";
        setup_mocks_for_should_not_process_airtime_send_txn();

        // Act
        transactionListener.listen(sendAirtimeTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    //

    @Test
    @Transactional
    @Rollback
    public void should_not_process_unknown_txn() throws Exception {
        //Setup
        String unknownTypeTxnId = "a8fe6f0d-420e-5121-a134-9c2342e36c64";

        // Act
        transactionListener.listen(unknownTypeTxnId, getHeaders());
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_process_airtime_send_txn() throws Exception {
        //Setup
        String senAirtimeTxnId = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        setup_mocks_for_should_not_process_airtime_send_txn();

        // Act
        transactionListener.listen(senAirtimeTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_process_airtime_send_txn_due_to_rest_client_error() throws Exception {
        // Setup
        String senAirtimeTxnId = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        setup_mocks_for_should_not_process_airtime_send_txn_due_to_rest_client_error();

        // Execute
        transactionListener.listen(senAirtimeTxnId, getHeaders());

        // Clear
        Mockito.clearInvocations(restTemplate);
    }

    private void setup_mocks_for_should_process_airtime_send_txn() throws Exception {
        AccountDebitResp mockAccountDebitResp = new AccountDebitResp(true, "Account debited");
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAccountServiceEndpoint()
                .concat("/account/debit/".concat(uid))), any(), eq(AccountDebitResp.class)))
                .thenReturn(new ResponseEntity<>(mockAccountDebitResp, HttpStatus.OK));

        AccountInfo mockAccountInfo = new AccountInfo();
        mockAccountInfo.setName("John Doe");
        mockAccountInfo.setEmail("email@email.com");
        mockAccountInfo.setPhoneNumber("+19999999999");

        Mockito.when(restTemplate.exchange(eq(properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid))),
                eq(HttpMethod.GET), any(), eq(AccountInfo.class)))
                .thenReturn(new ResponseEntity<>(mockAccountInfo, HttpStatus.OK));

        doNothing().when(notification).sendEmail(any(), any());
        doNothing().when(notification).sendSms(any(), any());
    }

    private void setup_mocks_for_should_process_money_load_txn() throws Exception {
        AccountCreditResp mockAccountCreditResp = new AccountCreditResp("Account Credited");
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAccountServiceEndpoint()
                .concat("/account/credit/".concat(uid))), any(), eq(AccountCreditResp.class)))
                .thenReturn(new ResponseEntity<>(mockAccountCreditResp, HttpStatus.OK));

        AccountInfo mockAccountInfo = new AccountInfo();
        mockAccountInfo.setName("John Doe");
        mockAccountInfo.setEmail("email@email.com");
        mockAccountInfo.setPhoneNumber("+19999999999");

        Mockito.when(restTemplate.exchange(eq(properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid))),
                eq(HttpMethod.GET), any(), eq(AccountInfo.class)))
                .thenReturn(new ResponseEntity<>(mockAccountInfo, HttpStatus.OK));

        doNothing().when(notification).sendEmail(any(), any());
        doNothing().when(notification).sendSms(any(), any());
    }

    private void setup_mocks_for_should_not_process_money_load_txn() throws Exception {
        AccountCreditResp mockAccountCreditResp = new AccountCreditResp("Account Credited");
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAccountServiceEndpoint()
                .concat("/account/credit/".concat(uid))), any(), eq(AccountCreditResp.class)))
                .thenReturn(new ResponseEntity<>(mockAccountCreditResp, HttpStatus.INTERNAL_SERVER_ERROR));

        AccountInfo mockAccountInfo = new AccountInfo();
        mockAccountInfo.setName("John Doe");
        mockAccountInfo.setEmail("email@email.com");
        mockAccountInfo.setPhoneNumber("+19999999999");

        Mockito.when(restTemplate.exchange(eq(properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid))),
                eq(HttpMethod.GET), any(), eq(AccountInfo.class)))
                .thenReturn(new ResponseEntity<>(mockAccountInfo, HttpStatus.OK));

        doThrow(new RuntimeException("Error")).when(notification).sendEmail(any(), any());
        doThrow(new RuntimeException("Error")).when(notification).sendSms(any(), any());
    }

    private void setup_mocks_for_should_not_process_airtime_send_txn() throws Exception {
        AccountDebitResp mockAccountDebitResp = new AccountDebitResp(true, "Account debited");
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAccountServiceEndpoint()
                .concat("/account/debit/".concat(uid))), any(), eq(AccountDebitResp.class)))
                .thenReturn(new ResponseEntity<>(mockAccountDebitResp, HttpStatus.INTERNAL_SERVER_ERROR));

        AccountInfo mockAccountInfo = new AccountInfo();
        mockAccountInfo.setName("John Doe");
        mockAccountInfo.setEmail("email@email.com");
        mockAccountInfo.setPhoneNumber("+19999999999");

        Mockito.when(restTemplate.exchange(eq(properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid))),
                eq(HttpMethod.GET), any(), eq(AccountInfo.class)))
                .thenReturn(new ResponseEntity<>(mockAccountInfo, HttpStatus.OK));

        doThrow(new NotificationException("Error")).when(notification).sendEmail(any(), any());
        doThrow(new NotificationException("Error")).when(notification).sendSms(any(), any());
    }

    private void setup_mocks_for_should_not_process_money_load_txn_due_to_rest_client_error() throws Exception {

        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAccountServiceEndpoint()
                .concat("/account/credit/".concat(uid))), any(), eq(AccountCreditResp.class)))
                .thenThrow(new RestClientException("Service down"));

        AccountInfo mockAccountInfo = new AccountInfo();
        mockAccountInfo.setName("John Doe");
        mockAccountInfo.setEmail("email@email.com");
        mockAccountInfo.setPhoneNumber("+19999999999");

        Mockito.when(restTemplate.exchange(eq(properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid))),
                eq(HttpMethod.GET), any(), eq(AccountInfo.class)))
                .thenReturn(new ResponseEntity<>(mockAccountInfo, HttpStatus.INTERNAL_SERVER_ERROR));

        doNothing().when(notification).sendEmail(any(), any());
        doNothing().when(notification).sendSms(any(), any());
    }

    private void setup_mocks_for_should_not_process_airtime_send_txn_due_to_rest_client_error() throws Exception {

        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";

        Mockito.when(restTemplate.postForEntity(eq(properties.getReloadlyAccountServiceEndpoint()
                .concat("/account/debit/".concat(uid))), any(), eq(AccountDebitResp.class)))
                .thenThrow(new RestClientException("Service down"));

        AccountInfo mockAccountInfo = new AccountInfo();
        mockAccountInfo.setName("John Doe");
        mockAccountInfo.setEmail("email@email.com");
        mockAccountInfo.setPhoneNumber("+19999999999");

        Mockito.when(restTemplate.exchange(eq(properties.getReloadlyAccountServiceEndpoint().concat("/account/".concat(uid))),
                eq(HttpMethod.GET), any(), eq(AccountInfo.class)))
                .thenThrow(new RestClientException("Service down"));

        doNothing().when(notification).sendEmail(any(), any());
        doNothing().when(notification).sendSms(any(), any());
    }

    private MessageHeaders getHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("kafka_receivedTimestamp", System.currentTimeMillis());
        headers.put("kafka_receivedTopic", "test-topic");
        headers.put("kafka_groupId", "test-group-id");
        headers.put("kafka_offset", 1L);
        headers.put("x-b3-spanid", "c9f621954448e21e");
        headers.put("x-b3-parentspanid", "6aa95a61699d289e");
        headers.put("x-b3-sampled", "1");
        headers.put("x-b3-traceid", "6aa95a61699d289e");
        return new MessageHeaders(headers);
    }
}
