package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends MongoRepository<PostJpaEntity, Long> {
    List<PostJpaEntity> findByPublishedTrue();
    Optional<PostJpaEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<PostJpaEntity> findTop10ByPublishedTrueOrderByIdDesc();
    List<PostJpaEntity> findByCategoryAndPublishedTrueAndIdNot(String category, Long excludeId, Pageable pageable);
    // Pagination
    List<PostJpaEntity> findByPublishedTrue(Pageable pageable);
    long countByPublishedTrue();
    List<PostJpaEntity> findAllBy(Pageable pageable);
    long count();
}
