package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {
    List<PostJpaEntity> findByPublishedTrue();
    Optional<PostJpaEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
