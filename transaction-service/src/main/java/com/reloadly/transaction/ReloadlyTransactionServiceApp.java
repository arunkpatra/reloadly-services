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
 * The Transaction Microservice Spring Boot App.
 *
 * <p>
 * The Transaction service allows users to initiate user transactions, like money reload and airtime send operations.
 * Note that, the transaction service, accepts user requests and posts to a reliable and resilient Kafka cluster. It
 * also persists the user request in persistent storage so that that its status can be queries. User requests return
 * immediately. The transactions posted to Kafka are picked up later asynchronously by the *Transaction Processor*
 * component.
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
