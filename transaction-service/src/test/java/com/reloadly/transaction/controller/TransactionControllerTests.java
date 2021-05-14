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

package com.reloadly.transaction.controller;

import com.reloadly.transaction.AbstractIntegrationTest;
import com.reloadly.transaction.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTests extends AbstractIntegrationTest {

    private final String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";

    @Test
    @Transactional
    @Rollback
    public void should_post_add_money_transaction() throws Exception {

        TransactionRequest request =
                new TransactionRequest(TransactionType.ADD_MONEY, new AddMoneyRequest(100f), null);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/transaction")
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        TransactionResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getTransactionId()).isNotEmpty();
        assertThat(response.getTransactionStatus()).isEqualTo(TransactionStatus.ACCEPTED);

        // Wait for Kafka to pickup transaction

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
    }

    @Test
    @Transactional
    @Rollback
    public void should_post_airtime_send_transaction() throws Exception {

        TransactionRequest request =
                new TransactionRequest(TransactionType.SEND_AIRTIME, null, new SendAirtimeRequest(100f, "+10000000000"));
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/transaction")
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        TransactionResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getTransactionId()).isNotEmpty();
        assertThat(response.getTransactionStatus()).isEqualTo(TransactionStatus.ACCEPTED);

        // Wait for Kafka to pickup transaction

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
    }

    @Test
    public void should_get_txn_status() throws Exception {
        String txnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";

        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/transaction/".concat(txnId))
                .header("X-Mock-UID", testUid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Assert
        TransactionResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getTransactionId()).isNotEmpty();
        assertThat(response.getTransactionStatus()).isEqualTo(TransactionStatus.ACCEPTED);
    }

    @Test
    public void should_not_get_txn_status() throws Exception {
        String txnId = "f7fe6f0d-420e-6161-a134-9c2342e36c33";

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/transaction/".concat(txnId))
                .header("X-Mock-UID", testUid))
                .andExpect(status().isInternalServerError()).andReturn();
    }

    @Test
    @Transactional
    @Rollback
    public void should_update_txn_status() throws Exception {
        String txnId = "e7fe6f0d-420e-5161-a134-9c2342e36c32";

        TransactionStatusUpdateRequest request = new TransactionStatusUpdateRequest(TransactionStatus.SUCCESSFUL);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(put("/transaction/".concat(txnId))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Assert
        TransactionResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionResponse.class);
        assertThat(response.getTransactionId()).isNotEmpty();
        assertThat(response.getTransactionStatus()).isEqualTo(TransactionStatus.SUCCESSFUL);
    }

    @Test
    public void should_not_update_txn_status() throws Exception {
        String txnId = "f7fe6f0d-420e-6161-a134-9c2342e36c33";

        TransactionStatusUpdateRequest request = new TransactionStatusUpdateRequest(TransactionStatus.SUCCESSFUL);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(put("/transaction/".concat(txnId))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }
}
