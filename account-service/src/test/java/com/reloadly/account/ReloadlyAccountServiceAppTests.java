package com.reloadly.account;

import com.reloadly.swagger.config.SwaggerUiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ReloadlyAccountServiceAppTests extends AbstractIntegrationTest {

    @Autowired
    private SwaggerUiProperties swaggerUiProperties;

    @Test
    void context_should_load() {
        assertThat(swaggerUiProperties.isEnabled()).isTrue();
    }
}
