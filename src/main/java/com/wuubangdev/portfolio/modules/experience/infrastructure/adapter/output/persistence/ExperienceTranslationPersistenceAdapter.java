package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.experience.domain.model.ExperienceTranslation;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperienceTranslationPersistenceAdapter implements ExperienceTranslationRepositoryPort {

    private static final String SEQUENCE_NAME = "experience_translations_sequence";

    private final ExperienceTranslationJpaRepository repository;
    private final MongoSequenceService sequenceService;

    @Override
    public ExperienceTranslation save(ExperienceTranslation translation) {
        ExperienceTranslationJpaEntity entity = toEntity(translation);
        if (entity.getId() == null) {
            repository.findByExperienceIdAndLocale(entity.getExperienceId(), entity.getLocale())
                    .ifPresent(existing -> entity.setId(existing.getId()));
        }
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repository.save(entity));
    }

    @Override
    public List<ExperienceTranslation> saveAll(List<ExperienceTranslation> translations) {
        return translations.stream().map(this::save).toList();
    }

    @Override
    public Optional<ExperienceTranslation> findByExperienceIdAndLocale(Long experienceId, String locale) {
        return repository.findByExperienceIdAndLocale(experienceId, locale).map(this::toDomain);
    }

    @Override
    public List<ExperienceTranslation> findByExperienceIdInAndLocale(List<Long> experienceIds, String locale) {
        return repository.findByExperienceIdInAndLocale(experienceIds, locale).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteByExperienceId(Long experienceId) {
        repository.deleteByExperienceId(experienceId);
    }

    private ExperienceTranslation toDomain(ExperienceTranslationJpaEntity entity) {
        return ExperienceTranslation.builder()
                .id(entity.getId())
                .experienceId(entity.getExperienceId())
                .locale(entity.getLocale())
                .company(entity.getCompany())
                .role(entity.getRole())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .build();
    }

    private ExperienceTranslationJpaEntity toEntity(ExperienceTranslation domain) {
        ExperienceTranslationJpaEntity entity = new ExperienceTranslationJpaEntity();
        entity.setId(domain.getId());
        entity.setExperienceId(domain.getExperienceId());
        entity.setLocale(domain.getLocale());
        entity.setCompany(domain.getCompany());
        entity.setRole(domain.getRole());
        entity.setDescription(domain.getDescription());
        entity.setLocation(domain.getLocation());
        return entity;
    }
}
