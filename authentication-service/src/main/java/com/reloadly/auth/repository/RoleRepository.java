package com.reloadly.auth.repository;

import com.reloadly.auth.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Role JPA repository.
 *
 * @author Arun Patra
 */
@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findOneByRoleNameIgnoreCase(String roleName);
}
