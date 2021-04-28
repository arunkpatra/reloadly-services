package com.reloadly.auth.repository;

import com.reloadly.auth.entity.UsernamePasswordCredentialsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Username password credentials JPA repository.
 *
 * @author Arun Patra
 */
@Repository
public interface UsernamePasswordCredentialsRepository extends CrudRepository<UsernamePasswordCredentialsEntity, Long> {
    Optional<UsernamePasswordCredentialsEntity> findByUsername(String username);
}
