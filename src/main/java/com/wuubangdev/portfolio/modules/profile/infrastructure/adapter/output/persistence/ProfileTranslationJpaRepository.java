package com.wuubangdev.portfolio.modules.profile.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileTranslationJpaRepository extends MongoRepository<ProfileTranslationJpaEntity, Long> {
    Optional<ProfileTranslationJpaEntity> findByProfileIdAndLocale(Long profileId, String locale);
    void deleteByProfileId(Long profileId);
}
