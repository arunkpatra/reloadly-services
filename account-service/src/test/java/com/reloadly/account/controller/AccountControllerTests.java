package com.reloadly.account.controller;

import com.reloadly.account.AbstractIntegrationTest;
import com.reloadly.account.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void should_update_existing_account() throws Exception{

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
    public void should_find_existing_account() throws Exception{
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
    public void should_not_find_non_existent_account() throws Exception{
        String testUid = "01fe6f0d-420e-4161-a134-9c2342e36c95";
        // Act and Assert
        mockMvc.perform(get("/account/".concat(testUid))
                .header("X-Mock-UID", testUid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
