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
