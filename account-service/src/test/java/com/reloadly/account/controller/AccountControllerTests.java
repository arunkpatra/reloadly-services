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

package com.reloadly.account.controller;

import com.reloadly.account.AbstractIntegrationTest;
import com.reloadly.account.model.*;
import com.reloadly.commons.model.ErrorResponse;
import com.reloadly.commons.model.account.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTests extends AbstractIntegrationTest {

    @Test
    @Transactional
    @Rollback
    public void should_create_new_account() throws Exception {

        String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";
        AccountInfo accountInfo = new AccountInfo("aa", "bb@email.com", "+911234567890");
        Address billingAddress = new Address("a", "b", "c", "d", "e", "f");
        AccountUpdateRequest request = new AccountUpdateRequest(accountInfo, billingAddress);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/account/".concat(testUid))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountUpdateResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountUpdateResponse.class);
        assertThat(response.getSuccessful()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Account update successful");
        assertThat(response.getAccountId()).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    public void should_update_existing_account() throws Exception {

        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        AccountInfo accountInfo = new AccountInfo("aa2", "bb2@email.com", "+911234567892");
        Address billingAddress = new Address("a2", "b2", "c2", "d2", "e2", "f2");
        AccountUpdateRequest request = new AccountUpdateRequest(accountInfo, billingAddress);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(put("/account/".concat(testUid))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountUpdateResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountUpdateResponse.class);
        assertThat(response.getSuccessful()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Account update successful");
        assertThat(response.getAccountId()).isNotNull();

    }

    @Test
    public void should_find_existing_account() throws Exception {
        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        String testAccountId = "f1fe6f0d-420e-4161-a134-9c2342e36c99";
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/account/".concat(testUid))
                .header("X-Mock-UID", testUid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountDetails response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountDetails.class);
        assertThat(response.getAccountId()).isEqualTo(testAccountId);
        assertThat(response.getAccountInfo()).isNotNull();
        assertThat(response.getBalance()).isNotNull();
        assertThat(response.getBillingAddress()).isNotNull();
        assertThat(response.getBalance().getAccountBalance()).isEqualTo(200.5f);
        assertThat(response.getAccountInfo().getName()).isEqualTo("John Doe");
        assertThat(response.getBillingAddress().getAddressLine1()).isEqualTo("1946 Sunset Blvd");
    }

    @Test
    public void should_not_find_non_existent_account() throws Exception {
        String testUid = "01fe6f0d-420e-4161-a134-9c2342e36c95";
        // Act and Assert
        mockMvc.perform(get("/account/".concat(testUid))
                .header("X-Mock-UID", testUid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void should_get_account_balance() throws Exception {
        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/account/balance/".concat(testUid))
                .header("X-Mock-UID", testUid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountBalance response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountBalance.class);
        assertThat(response.getAccountBalance()).isEqualTo(200.5f);
    }

    @Test
    @Transactional
    @Rollback
    public void should_credit_account() throws Exception {

        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        AccountCreditReq request = new AccountCreditReq(150.5f);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/account/credit/".concat(testUid))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountCreditResp response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountCreditResp.class);
        assertThat(response.getMessage()).isEqualTo("Account successfully credited");
    }

    @Test
    @Transactional
    @Rollback
    public void should_debit_account() throws Exception {

        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        AccountDebitReq request = new AccountDebitReq(10.5f);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/account/debit/".concat(testUid))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountDebitResp response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountDebitResp.class);
        assertThat(response.getSuccessful()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Account successfully debited");
    }

    @Test
    @Transactional
    @Rollback
    public void should_not_debit_account() throws Exception {
        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        AccountDebitReq request = new AccountDebitReq(500f);
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(post("/account/debit/".concat(testUid))
                .header("X-Mock-UID", testUid).content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        ErrorResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(response.getErrorDescription()).isEqualTo("Insufficient funds");
    }

    @Test
    public void should_get_account_info() throws Exception {
        String testUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        // Setup and Act
        MvcResult mvcResult = mockMvc.perform(get("/account/info/".concat(testUid))
                .header("X-Mock-UID", testUid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AccountInfo response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountInfo.class);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("email@email.com");
        assertThat(response.getPhoneNumber()).isEqualTo("+19999999999");
    }

}
