package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.post.domain.model.Comment;
import com.wuubangdev.portfolio.modules.post.domain.port.CommentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements CommentRepositoryPort {

    private static final String SEQUENCE_NAME = "comments_sequence";

    private final CommentJpaRepository repo;
    private final CommentMapper mapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Comment save(Comment comment) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        CommentJpaEntity entity = mapper.toEntity(comment);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return mapper.toDomain(repo.save(entity));
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return repo.findByPostId(postId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
