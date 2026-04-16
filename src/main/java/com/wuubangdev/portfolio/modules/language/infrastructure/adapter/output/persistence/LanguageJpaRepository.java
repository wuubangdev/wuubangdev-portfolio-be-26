package com.wuubangdev.portfolio.modules.language.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LanguageJpaRepository extends MongoRepository<LanguageJpaEntity, Long> {
    Optional<LanguageJpaEntity> findByCode(String code);
}
