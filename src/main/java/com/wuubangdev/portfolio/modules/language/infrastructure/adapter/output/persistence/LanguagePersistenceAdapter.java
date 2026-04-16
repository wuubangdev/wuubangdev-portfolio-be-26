package com.wuubangdev.portfolio.modules.language.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.language.domain.model.Language;
import com.wuubangdev.portfolio.modules.language.domain.port.LanguageRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LanguagePersistenceAdapter implements LanguageRepositoryPort {

    private static final String SEQUENCE_NAME = "languages_sequence";

    private final LanguageJpaRepository repo;
    private final LanguageMapper mapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Language save(Language language) {
        LanguageJpaEntity entity = mapper.toEntity(language);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return mapper.toDomain(repo.save(entity));
    }

    @Override
    public List<Language> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Language> findById(Long id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Language> findByCode(String code) {
        return repo.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
