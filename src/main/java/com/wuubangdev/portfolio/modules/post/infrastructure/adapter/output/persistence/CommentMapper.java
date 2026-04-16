package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.post.domain.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toDomain(CommentJpaEntity entity) {
        return Comment.builder()
                .id(entity.getId())
                .postId(entity.getPostId())
                .author(entity.getAuthor())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .parentId(entity.getParentId())
                .build();
    }

    public CommentJpaEntity toEntity(Comment domain) {
        CommentJpaEntity entity = new CommentJpaEntity();
        entity.setId(domain.getId());
        entity.setPostId(domain.getPostId());
        entity.setAuthor(domain.getAuthor());
        entity.setContent(domain.getContent());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setParentId(domain.getParentId());
        return entity;
    }
}
