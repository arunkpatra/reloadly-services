package com.reloadly.transaction.repository;

import com.reloadly.transaction.entity.AirtimeSendTxnEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirtimeSendTxnRepository extends CrudRepository<AirtimeSendTxnEntity, Long> {

}
