package com.reloadly.transaction.repository;

import com.reloadly.transaction.entity.AirtimeSendTxnEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The Airtime send JPA repository.
 *
 * @author Arun Patra
 */
@Repository
public interface AirtimeSendTxnRepository extends CrudRepository<AirtimeSendTxnEntity, Long> {

    Optional<AirtimeSendTxnEntity> getByTxnId(String txnId);

}
