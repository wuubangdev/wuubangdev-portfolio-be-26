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
                .tags(e.getTags()).published(e.getPublished())
                .author(e.getAuthor())
                .titleSeo(e.getTitleSeo())
                .descriptionSeo(e.getDescriptionSeo())
                .thumbnailSeo(e.getThumbnailSeo())
                .seoKeywords(e.getSeoKeywords())
                .canonicalUrl(e.getCanonicalUrl())
                .indexable(e.getIndexable())
                .likes(e.getLikes())
                .hearts(e.getHearts())
                .commentsCount(e.getCommentsCount())
                .shares(e.getShares())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .displayOrder(e.getDisplayOrder())
                .isHidden(e.getIsHidden())
                .build();
    }

    private PostJpaEntity toEntity(Post p) {
        PostJpaEntity e = new PostJpaEntity();
        e.setId(p.getId()); e.setTitle(p.getTitle()); e.setSlug(p.getSlug());
        e.setCategory(p.getCategory());
        e.setContent(p.getContent()); e.setSummary(p.getSummary()); e.setCoverImageUrl(p.getCoverImageUrl());
        e.setTags(p.getTags()); e.setPublished(p.getPublished());
        e.setAuthor(p.getAuthor());
        e.setTitleSeo(p.getTitleSeo());
        e.setDescriptionSeo(p.getDescriptionSeo());
        e.setThumbnailSeo(p.getThumbnailSeo());
        e.setSeoKeywords(p.getSeoKeywords());
        e.setCanonicalUrl(p.getCanonicalUrl());
        e.setIndexable(p.getIndexable());
        e.setLikes(p.getLikes());
        e.setHearts(p.getHearts());
        e.setCommentsCount(p.getCommentsCount());
        e.setShares(p.getShares());
        e.setStatus(p.getStatus());
        e.setDisplayOrder(p.getDisplayOrder());
        e.setIsHidden(p.getIsHidden());
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
    @Override public List<Post> findRecentPosts(int limit) { return repo.findTop10ByPublishedTrueOrderByIdDesc().stream().limit(limit).map(this::toDomain).toList(); }
    @Override public List<Post> findRelatedPosts(String category, Long excludeId, int limit) { return repo.findByCategoryAndPublishedTrueAndIdNot(category, excludeId, org.springframework.data.domain.PageRequest.of(0, limit)).stream().map(this::toDomain).toList(); }
    @Override public List<Post> findAllPublishedPaged(int page, int size) { return repo.findByPublishedTrue(org.springframework.data.domain.PageRequest.of(page, size)).stream().map(this::toDomain).toList(); }
    @Override public long countAllPublished() { return repo.countByPublishedTrue(); }
    @Override public List<Post> findAllPaged(int page, int size) { return repo.findAll(org.springframework.data.domain.PageRequest.of(page, size)).stream().map(this::toDomain).toList(); }
    @Override public long countAll() { return repo.count(); }
}
