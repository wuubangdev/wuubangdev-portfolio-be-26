package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectJpaRepository extends MongoRepository<ProjectJpaEntity, Long> {
    Optional<ProjectJpaEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
