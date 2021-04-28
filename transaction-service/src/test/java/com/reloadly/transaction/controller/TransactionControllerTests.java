package com.reloadly.transaction.controller;

import com.reloadly.transaction.AbstractIntegrationTest;
import com.reloadly.transaction.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTests extends AbstractIntegrationTest {

    @Test
//    @Transactional
//    @Rollback
    public void should_post_transaction() throws Exception {
        String testUid = "fefe6f0d-420e-4161-a134-9c2342e36c95";

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
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
    }
}
