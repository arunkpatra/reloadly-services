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
