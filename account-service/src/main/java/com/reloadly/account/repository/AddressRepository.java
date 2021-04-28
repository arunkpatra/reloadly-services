package com.reloadly.account.repository;

import com.reloadly.account.entity.AddressEntity;
import com.reloadly.account.model.AddressType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository.
 *
 * @author Arun Patra
 */
@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    /**
     * Find all addresses for an account.
     *
     * @param accountId The account ID
     * @return A list of addresses.
     */
    List<AddressEntity> findByAccountId(String accountId);

    Optional<AddressEntity> findByAccountIdAndAddressType(String accountId, AddressType addressType);
}
