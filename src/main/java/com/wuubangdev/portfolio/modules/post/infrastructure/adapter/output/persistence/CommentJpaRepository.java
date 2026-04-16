package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentJpaRepository extends MongoRepository<CommentJpaEntity, Long> {
    List<CommentJpaEntity> findByPostId(Long postId);
}
