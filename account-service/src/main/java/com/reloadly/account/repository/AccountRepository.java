package com.reloadly.account.repository;

import com.reloadly.account.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountId(String accountId);
    Optional<AccountEntity> findByUid(String uid);
}
