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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTests extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void should_find_user_by_uid() {
        String existingUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        assertThat(userRepository.findByUid(existingUid).isPresent()).isTrue();
        assertThat(userRepository.findByUid(existingUid).get().getActive()).isTrue();
        assertThat(userRepository.findByUid(existingUid).get().getAuthorityEntities().size()).isEqualTo(2);
    }

    @Test
    public void should_not_find_non_existent_user() {
        String existingUid = "d1fe6f0d-420e-4161-a134-9c2342e36c18";
        assertThat(userRepository.findByUid(existingUid).isPresent()).isFalse();
    }

    @Test
    @Transactional
    public void should_find_api_keys() {
        String existingUid = "c1fe6f0d-420e-4161-a134-9c2342e36c95";
        assertThat(userRepository.findByUid(existingUid).isPresent()).isTrue();
        assertThat(userRepository.findByUid(existingUid).get().getApiKeyEntities().size()).isGreaterThan(0);
    }

}
