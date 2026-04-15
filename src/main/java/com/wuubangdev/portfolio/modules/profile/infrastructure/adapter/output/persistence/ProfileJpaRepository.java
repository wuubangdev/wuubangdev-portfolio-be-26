package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileJpaRepository extends MongoRepository<ProfileJpaEntity, Long> {
    Optional<ProfileJpaEntity> findFirstByOrderByIdAsc();
}
