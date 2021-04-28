package com.reloadly.account.repository;

import com.reloadly.account.entity.AccountBalanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Spring Data JPA repository.
 *
 * @author Arun Patra
 */
@Repository
public interface AccountBalanceRepository extends CrudRepository<AccountBalanceEntity, Long> {
    Optional<AccountBalanceEntity> findByAccountId(String accountId);
}
