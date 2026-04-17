package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EducationTranslationJpaRepository extends MongoRepository<EducationTranslationJpaEntity, Long> {
    Optional<EducationTranslationJpaEntity> findByEducationIdAndLocale(Long educationId, String locale);
    List<EducationTranslationJpaEntity> findByEducationIdInAndLocale(List<Long> educationIds, String locale);
    void deleteByEducationId(Long educationId);
}
