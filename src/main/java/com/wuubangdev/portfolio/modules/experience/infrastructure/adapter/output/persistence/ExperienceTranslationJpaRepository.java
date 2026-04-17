package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceTranslationJpaRepository extends MongoRepository<ExperienceTranslationJpaEntity, Long> {
    Optional<ExperienceTranslationJpaEntity> findByExperienceIdAndLocale(Long experienceId, String locale);
    List<ExperienceTranslationJpaEntity> findByExperienceIdInAndLocale(List<Long> experienceIds, String locale);
    void deleteByExperienceId(Long experienceId);
}
