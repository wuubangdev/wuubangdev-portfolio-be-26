package com.wuubangdev.portfolio.modules.project.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.project.domain.model.ProjectTranslation;
import com.wuubangdev.portfolio.modules.project.domain.port.ProjectTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectTranslationPersistenceAdapter implements ProjectTranslationRepositoryPort {

    private static final String SEQUENCE_NAME = "project_translations_sequence";

    private final ProjectTranslationJpaRepository repository;
    private final MongoSequenceService sequenceService;

    @Override
    public ProjectTranslation save(ProjectTranslation translation) {
        ProjectTranslationJpaEntity entity = toEntity(translation);
        if (entity.getId() == null) {
            repository.findByProjectIdAndLocale(entity.getProjectId(), entity.getLocale())
                    .ifPresent(existing -> entity.setId(existing.getId()));
        }
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repository.save(entity));
    }

    @Override
    public List<ProjectTranslation> saveAll(List<ProjectTranslation> translations) {
        return translations.stream().map(this::save).toList();
    }

    @Override
    public Optional<ProjectTranslation> findByProjectIdAndLocale(Long projectId, String locale) {
        return repository.findByProjectIdAndLocale(projectId, locale).map(this::toDomain);
    }

    @Override
    public List<ProjectTranslation> findByProjectIdInAndLocale(List<Long> projectIds, String locale) {
        return repository.findByProjectIdInAndLocale(projectIds, locale).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteByProjectId(Long projectId) {
        repository.deleteByProjectId(projectId);
    }

    private ProjectTranslation toDomain(ProjectTranslationJpaEntity entity) {
        return ProjectTranslation.builder()
                .id(entity.getId())
                .projectId(entity.getProjectId())
                .locale(entity.getLocale())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .content(entity.getContent())
                .titleSeo(entity.getTitleSeo())
                .descriptionSeo(entity.getDescriptionSeo())
                .seoKeywords(entity.getSeoKeywords())
                .build();
    }

    private ProjectTranslationJpaEntity toEntity(ProjectTranslation domain) {
        ProjectTranslationJpaEntity entity = new ProjectTranslationJpaEntity();
        entity.setId(domain.getId());
        entity.setProjectId(domain.getProjectId());
        entity.setLocale(domain.getLocale());
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setContent(domain.getContent());
        entity.setTitleSeo(domain.getTitleSeo());
        entity.setDescriptionSeo(domain.getDescriptionSeo());
        entity.setSeoKeywords(domain.getSeoKeywords());
        return entity;
    }
}
