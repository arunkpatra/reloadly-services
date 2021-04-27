package com.reloadly.transaction.repository;

import com.reloadly.transaction.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByTxnId(String txnId);

    List<TransactionEntity> findByUid(String uid);
}
