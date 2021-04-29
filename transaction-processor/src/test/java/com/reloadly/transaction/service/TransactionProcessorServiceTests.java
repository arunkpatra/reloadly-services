package com.reloadly.transaction.service;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.commons.model.account.AccountCreditResp;
import com.reloadly.commons.model.account.AccountDebitResp;
import com.reloadly.commons.model.account.AccountInfo;
import com.reloadly.transaction.AbstractIntegrationTest;
import com.reloadly.transaction.config.TransactionProcessorProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

public class TransactionProcessorServiceTests extends AbstractIntegrationTest {

    @Autowired
    protected TransactionProcessorProperties properties;
    @Autowired
    private TransactionProcessorService tps;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReloadlyNotification notification;

    @Test
    @Transactional
    @Rollback
    public void should_process_money_load_txn() throws Exception {
        // Setup
        String moneyReloadTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        setup_mocks_for_should_process_money_load_txn();
        // Execute
        tps.processInboundTransaction(moneyReloadTxnId);
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

    @Test
    @Transactional
    @Rollback
    public void should_process_airtime_send_txn() throws Exception {
        //Setup
        String senAirtimeTxnId = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        setup_mocks_for_should_process_airtime_send_txn();

        // Act
        tps.processInboundTransaction(senAirtimeTxnId);
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
}
