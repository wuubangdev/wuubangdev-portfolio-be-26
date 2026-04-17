package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostTranslationJpaRepository extends MongoRepository<PostTranslationJpaEntity, Long> {
    Optional<PostTranslationJpaEntity> findByPostIdAndLocale(Long postId, String locale);
    List<PostTranslationJpaEntity> findByPostIdInAndLocale(List<Long> postIds, String locale);
    void deleteByPostId(Long postId);
}
