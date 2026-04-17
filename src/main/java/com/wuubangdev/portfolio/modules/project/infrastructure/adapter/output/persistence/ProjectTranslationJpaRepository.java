package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTranslationJpaRepository extends MongoRepository<ProjectTranslationJpaEntity, Long> {
    Optional<ProjectTranslationJpaEntity> findByProjectIdAndLocale(Long projectId, String locale);
    List<ProjectTranslationJpaEntity> findByProjectIdInAndLocale(List<Long> projectIds, String locale);
    void deleteByProjectId(Long projectId);
}
