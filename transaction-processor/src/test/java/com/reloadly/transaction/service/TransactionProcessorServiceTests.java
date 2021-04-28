package com.reloadly.transaction.service;

import com.reloadly.autoconfig.notification.service.ReloadlyNotification;
import com.reloadly.transaction.AbstractIntegrationTest;
import com.reloadly.transaction.processor.moneyreload.MoneyReloadTransactionProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TransactionProcessorService tps;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ReloadlyNotification notification;

    @Test
    @Transactional
    @Rollback
    public void should_process_money_load_txn() throws Exception{
        // TODO: Fix the mock
        MoneyReloadTransactionProcessor.AccountCreditResponse mockResponse =
                new MoneyReloadTransactionProcessor.AccountCreditResponse("Account Credited");

        Mockito.when(restTemplate.postForEntity(any(), any(), eq(MoneyReloadTransactionProcessor.AccountCreditResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        doNothing().when(notification).sendEmail(any(), any());
        doNothing().when(notification).sendSms(any(), any());

        String moneyReloadTxnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";
        tps.processInboundTransaction(moneyReloadTxnId);

        Mockito.clearInvocations(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void should_process_airtime_send_txn() throws Exception{
        // TODO: Fix with mocks
        String moneyReloadTxnId = "b7fe6f0d-420e-5121-a134-9c2342e36c64";
        tps.processInboundTransaction(moneyReloadTxnId);
    }
}
