package com.reloadly.auth.encoder;

import com.reloadly.auth.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest extends AbstractIntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @Autowired
    PasswordEncoder encoder;

    @Test
    public void test_password_encryption() {
        assertThat(encoder).isNotNull();
        String result = encoder.encode("test-api-key-2");
        assertThat(result.equals("test-api-key-2")).isFalse();
        assertThat(encoder.matches("test-api-key-2", result)).isTrue();
        LOGGER.info("Password= test-api-key-2, Encoded= " + result);
    }
}
