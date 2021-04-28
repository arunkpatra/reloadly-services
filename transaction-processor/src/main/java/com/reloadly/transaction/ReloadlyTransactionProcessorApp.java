package com.reloadly.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Transaction Processor.
 *
 * <p>
 * The Transaction Processor is at the heart of the platform and has the responsibility of processing inbound user
 * transactions. The transactions could be related to any business functionality, like sending airtime or reloading
 * money into your own account. Business operations that are ideally suited to be routed to the transaction processor
 * are those that are potentially expensive and have strict guarantees to fulfill. Also, such operations may be retried
 * e.g. a airtime send operation may be retried.
 * <p>
 * The transaction processor listens to transaction requests that are processed to Kafka. Part of the objective here
 * is to scale the transaction processing horizontally. Kafka listeners(and the transaction processing is one) can
 * for consumer groups. Essentially, when the transaction processor is deployed for example to a Kubernetes cluster,
 * one could scale very easily by increasing/decreasing the number of replicas.
 * <p>
 * All the other production grade features of Kafka are available by default to the transaction processor.
 * </p>
 *
 * @author Arun Patra
 */
@SpringBootApplication
public class ReloadlyTransactionProcessorApp {

    public static void main(String[] args) {
        SpringApplication.run(ReloadlyTransactionProcessorApp.class, args);
    }

}
