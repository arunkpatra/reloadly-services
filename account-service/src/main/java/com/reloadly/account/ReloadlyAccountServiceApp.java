package com.reloadly.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Account Microservice Spring Boot App.
 *
 * <p>
 *     The Account microservice, allows interacting with Account domain model. Various update and query operations can
 *     be performed. Se Swagger API documentation in Swagger UI.
 * </p>
 *
 * @author Arun Patra
 */
@SpringBootApplication
public class ReloadlyAccountServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(ReloadlyAccountServiceApp.class, args);
    }

}
