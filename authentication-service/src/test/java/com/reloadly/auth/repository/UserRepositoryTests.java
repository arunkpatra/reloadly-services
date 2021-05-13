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
