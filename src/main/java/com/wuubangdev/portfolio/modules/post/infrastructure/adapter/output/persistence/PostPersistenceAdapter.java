package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.post.domain.model.Post;
import com.wuubangdev.portfolio.modules.post.domain.port.PostRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostRepositoryPort {

    private static final String SEQUENCE_NAME = "posts_sequence";

    private final PostJpaRepository repo;
    private final MongoSequenceService sequenceService;

    private Post toDomain(PostJpaEntity e) {
        return Post.builder().id(e.getId()).title(e.getTitle()).slug(e.getSlug())
                .category(e.getCategory())
                .content(e.getContent()).summary(e.getSummary()).coverImageUrl(e.getCoverImageUrl())
                .tags(e.getTags()).published(e.getPublished()).build();
    }

    private PostJpaEntity toEntity(Post p) {
        PostJpaEntity e = new PostJpaEntity();
        e.setId(p.getId()); e.setTitle(p.getTitle()); e.setSlug(p.getSlug());
        e.setCategory(p.getCategory());
        e.setContent(p.getContent()); e.setSummary(p.getSummary()); e.setCoverImageUrl(p.getCoverImageUrl());
        e.setTags(p.getTags()); e.setPublished(p.getPublished());
        return e;
    }

    @Override
    public Post save(Post p) {
        PostJpaEntity entity = toEntity(p);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repo.save(entity));
    }
    @Override public List<Post> findAllPublished() { return repo.findByPublishedTrue().stream().map(this::toDomain).toList(); }
    @Override public List<Post> findAll() { return repo.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Post> findById(Long id) { return repo.findById(id).map(this::toDomain); }
    @Override public Optional<Post> findBySlug(String slug) { return repo.findBySlug(slug).map(this::toDomain); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
    @Override public boolean existsBySlug(String slug) { return repo.existsBySlug(slug); }
}
