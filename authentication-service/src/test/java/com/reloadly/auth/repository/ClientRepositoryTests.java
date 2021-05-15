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
import com.reloadly.auth.entity.ClientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void should_find_client_id() {
        String clientId = "bafa4494-40dd-4b0c-b42e-623399e70533";
        Optional<ClientEntity> clientEntity = clientRepository.findByClientId(clientId);
        assertThat(clientEntity.isPresent()).isTrue();
        assertThat(clientEntity.get().getClientId()).isEqualTo(clientId);
        assertThat(clientEntity.get().getUid()).isEqualTo("c1fe6f0d-420e-4161-a134-9c2342e36c95");
        assertThat(clientEntity.get().getApiKeyEntities().size()).isEqualTo(3);
    }

    @Test
    public void should_find_client_id_by_uid() {
        String uid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        Optional<ClientEntity> clientEntity = clientRepository.findByUid(uid);
        assertThat(clientEntity.isPresent()).isTrue();
        assertThat(clientEntity.get().getClientId()).isEqualTo("bafa4494-40dd-4b0c-b42e-623399e70533");
    }

    @Test
    @Transactional
    @Rollback
    public void should_save_client_id() {
        String clientId = "cafa4494-40dd-4b0c-b42e-623399e70533";
        ClientEntity ce = new ClientEntity();
        ce.setClientId(clientId);
        ce.setUid("d273e6f9-bbf5-4126-bd7b-643ffc601291");
        ce = clientRepository.save(ce);
        assertThat(ce).isNotNull();
    }

}
