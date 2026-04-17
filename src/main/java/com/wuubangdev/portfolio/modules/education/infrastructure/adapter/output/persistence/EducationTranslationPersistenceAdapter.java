package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.education.domain.model.EducationTranslation;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EducationTranslationPersistenceAdapter implements EducationTranslationRepositoryPort {

    private static final String SEQUENCE_NAME = "education_translations_sequence";

    private final EducationTranslationJpaRepository repository;
    private final MongoSequenceService sequenceService;

    @Override
    public EducationTranslation save(EducationTranslation translation) {
        EducationTranslationJpaEntity entity = toEntity(translation);
        if (entity.getId() == null) {
            repository.findByEducationIdAndLocale(entity.getEducationId(), entity.getLocale())
                    .ifPresent(existing -> entity.setId(existing.getId()));
        }
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repository.save(entity));
    }

    @Override
    public List<EducationTranslation> saveAll(List<EducationTranslation> translations) {
        return translations.stream().map(this::save).toList();
    }

    @Override
    public Optional<EducationTranslation> findByEducationIdAndLocale(Long educationId, String locale) {
        return repository.findByEducationIdAndLocale(educationId, locale).map(this::toDomain);
    }

    @Override
    public List<EducationTranslation> findByEducationIdInAndLocale(List<Long> educationIds, String locale) {
        return repository.findByEducationIdInAndLocale(educationIds, locale).stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteByEducationId(Long educationId) {
        repository.deleteByEducationId(educationId);
    }

    private EducationTranslation toDomain(EducationTranslationJpaEntity entity) {
        return EducationTranslation.builder()
                .id(entity.getId())
                .educationId(entity.getEducationId())
                .locale(entity.getLocale())
                .institution(entity.getInstitution())
                .degree(entity.getDegree())
                .fieldOfStudy(entity.getFieldOfStudy())
                .description(entity.getDescription())
                .location(entity.getLocation())
                .build();
    }

    private EducationTranslationJpaEntity toEntity(EducationTranslation domain) {
        EducationTranslationJpaEntity entity = new EducationTranslationJpaEntity();
        entity.setId(domain.getId());
        entity.setEducationId(domain.getEducationId());
        entity.setLocale(domain.getLocale());
        entity.setInstitution(domain.getInstitution());
        entity.setDegree(domain.getDegree());
        entity.setFieldOfStudy(domain.getFieldOfStudy());
        entity.setDescription(domain.getDescription());
        entity.setLocation(domain.getLocation());
        return entity;
    }
}
