package com.reloadly.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Transaction Microservice Spring Boot App.
 *
 * <p>
 *     The Transaction service allows users to initiate user transactions, like money reload and airtime send operations.
 *     Note that, the transaction service, accepts user requests and posts to a reliable and resilient Kafka cluster. It
 *     also persists the user request in persistent storage so that that its status can be queries. User requests return
 *     immediately. The transactions posted to Kafka are picked up later asynchronously by the *Transaction Processor*
 *     component.
 * </p>
 *
 * @author Arun Patra
 */
@SpringBootApplication
public class ReloadlyTransactionServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ReloadlyTransactionServiceApp.class, args);
    }

}
