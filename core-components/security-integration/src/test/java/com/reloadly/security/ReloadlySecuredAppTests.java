package com.reloadly.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
//import static org.assertj.core.api.Assertions.assertThat;

class ReloadlySecuredAppTests extends AbstractIntegrationTest{

    @Autowired
    private ApplicationContext context;

    @Test
    void context_should_load() {

    }
}
