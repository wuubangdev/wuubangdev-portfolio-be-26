package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.post.domain.model.PostTranslation;
import com.wuubangdev.portfolio.modules.post.domain.port.PostTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostTranslationPersistenceAdapter implements PostTranslationRepositoryPort {

    private static final String SEQUENCE_NAME = "post_translations_sequence";

    private final PostTranslationJpaRepository repository;
    private final MongoSequenceService sequenceService;

    @Override
    public PostTranslation save(PostTranslation translation) {
        PostTranslationJpaEntity entity = toEntity(translation);
        if (entity.getId() == null) {
            repository.findByPostIdAndLocale(entity.getPostId(), entity.getLocale())
                    .ifPresent(existing -> entity.setId(existing.getId()));
        }
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repository.save(entity));
    }

    @Override
    public List<PostTranslation> saveAll(List<PostTranslation> translations) {
        return translations.stream().map(this::save).toList();
    }

    @Override
    public Optional<PostTranslation> findByPostIdAndLocale(Long postId, String locale) {
        return repository.findByPostIdAndLocale(postId, locale).map(this::toDomain);
    }

    @Override
    public List<PostTranslation> findByPostIdInAndLocale(List<Long> postIds, String locale) {
        return repository.findByPostIdInAndLocale(postIds, locale).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteByPostId(Long postId) {
        repository.deleteByPostId(postId);
    }

    private PostTranslation toDomain(PostTranslationJpaEntity entity) {
        return PostTranslation.builder()
                .id(entity.getId())
                .postId(entity.getPostId())
                .locale(entity.getLocale())
                .title(entity.getTitle())
                .content(entity.getContent())
                .summary(entity.getSummary())
                .titleSeo(entity.getTitleSeo())
                .descriptionSeo(entity.getDescriptionSeo())
                .seoKeywords(entity.getSeoKeywords())
                .build();
    }

    private PostTranslationJpaEntity toEntity(PostTranslation domain) {
        PostTranslationJpaEntity entity = new PostTranslationJpaEntity();
        entity.setId(domain.getId());
        entity.setPostId(domain.getPostId());
        entity.setLocale(domain.getLocale());
        entity.setTitle(domain.getTitle());
        entity.setContent(domain.getContent());
        entity.setSummary(domain.getSummary());
        entity.setTitleSeo(domain.getTitleSeo());
        entity.setDescriptionSeo(domain.getDescriptionSeo());
        entity.setSeoKeywords(domain.getSeoKeywords());
        return entity;
    }
}
