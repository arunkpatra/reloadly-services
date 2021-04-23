package com.reloadly.auth.repository;

import com.reloadly.auth.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    @Transactional(readOnly = true)
    List<AuthorityEntity> findAllByUid(String uid);
}
