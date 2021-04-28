package com.reloadly.transaction.repository;

import com.reloadly.transaction.entity.MoneyReloadTxnEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * The Money reload JPA repository.
 *
 * @author Arun Patra
 */
@Repository
public interface MoneyReloadTxnRepository extends CrudRepository<MoneyReloadTxnEntity, Long> {

    Optional<MoneyReloadTxnEntity> getByTxnId(String txnId);

}
