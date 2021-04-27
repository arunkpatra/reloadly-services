package com.reloadly.auth.repository;

import com.reloadly.auth.entity.ApiKeyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKeyEntity, Long> {

    @Transactional(readOnly = true)
    Optional<ApiKeyEntity> findByApiKey(String apiKey);
}
