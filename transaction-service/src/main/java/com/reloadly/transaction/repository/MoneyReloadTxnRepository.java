package com.reloadly.transaction.repository;

import com.reloadly.transaction.entity.MoneyReloadTxnEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyReloadTxnRepository extends CrudRepository<MoneyReloadTxnEntity, Long> {

}
