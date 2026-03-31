package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, Long> {
    Optional<ProjectJpaEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
