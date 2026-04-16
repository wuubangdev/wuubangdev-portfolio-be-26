package com.wuubangdev.portfolio.modules.social.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SocialJpaRepository extends MongoRepository<SocialJpaEntity, Long> {
    Optional<SocialJpaEntity> findFirstByOrderByIdAsc();
}
