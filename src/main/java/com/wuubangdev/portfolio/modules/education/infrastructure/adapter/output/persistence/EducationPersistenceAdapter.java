package com.wuubangdev.portfolio.modules.education.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.education.domain.model.Education;
import com.wuubangdev.portfolio.modules.education.domain.port.EducationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EducationPersistenceAdapter implements EducationRepositoryPort {

    private static final String SEQUENCE_NAME = "educations_sequence";

    private final EducationJpaRepository repo;
    private final EducationMapper mapper;
    private final MongoSequenceService sequenceService;

    @Override
    public Education save(Education education) {
        EducationJpaEntity entity = mapper.toEntity(education);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return mapper.toDomain(repo.save(entity));
    }

    @Override
    public List<Education> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Education> findById(Long id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
