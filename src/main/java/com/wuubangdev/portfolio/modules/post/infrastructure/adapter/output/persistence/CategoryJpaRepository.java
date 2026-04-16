package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends MongoRepository<CategoryJpaEntity, Long> {
    Optional<CategoryJpaEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
