package com.reloadly.auth.repository;

import com.reloadly.auth.AbstractIntegrationTest;
import com.reloadly.auth.entity.ApiKeyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiKeyRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Test
    public void should_find_api_key() {
        String apiKey = "d3fe6f0d-120e-4161-a134-8c2342e36ca6";
        assertThat(apiKeyRepository.findByApiKey(apiKey).isPresent()).isTrue();
        assertThat(apiKeyRepository.findByApiKey(apiKey).get().getActive()).isTrue();
        assertThat(apiKeyRepository.findByApiKey(apiKey).get().getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
    }

    @Test
    @Transactional
    @Rollback
    public void should_save_api_keys() {
        String apiKey = UUID.randomUUID().toString();
        ApiKeyEntity key = new ApiKeyEntity();
        key.setUid("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        key.setApiKey(apiKey);
        key.setActive(true);
        assertThat(apiKeyRepository.save(key)).isNotNull();
    }

}
