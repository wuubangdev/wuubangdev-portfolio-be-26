package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectJpaRepository extends MongoRepository<ProjectJpaEntity, Long> {
    List<ProjectJpaEntity> findAllByOrderByDisplayOrderAscIdDesc();
    List<ProjectJpaEntity> findByCategoryIgnoreCaseOrderByDisplayOrderAscIdDesc(String category);
    List<ProjectJpaEntity> findByFeaturedTrueOrderByDisplayOrderAscIdDesc();
    List<ProjectJpaEntity> findAllBy(Pageable pageable);
    List<ProjectJpaEntity> findByCategoryIgnoreCase(String category, Pageable pageable);
    List<ProjectJpaEntity> findByFeaturedTrue(Pageable pageable);
    long countByCategoryIgnoreCase(String category);
    long countByFeaturedTrue();
    List<ProjectJpaEntity> findByCategoryIgnoreCaseAndIdNot(String category, Long excludeId, Pageable pageable);
    Optional<ProjectJpaEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
