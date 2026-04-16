package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.database.MongoSequenceService;
import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperiencePersistenceAdapter implements ExperienceRepositoryPort {

    private static final String SEQUENCE_NAME = "experiences_sequence";

    private final ExperienceJpaRepository repo;
    private final MongoSequenceService sequenceService;

    private Experience toDomain(ExperienceJpaEntity e) {
        return Experience.builder().id(e.getId()).company(e.getCompany()).companyUrl(e.getCompanyUrl()).role(e.getRole())
                .description(e.getDescription()).logoUrl(e.getLogoUrl()).startDate(e.getStartDate())
                .endDate(e.getEndDate()).location(e.getLocation()).displayOrder(e.getDisplayOrder()).isHidden(e.getIsHidden()).build();
    }

    private ExperienceJpaEntity toEntity(Experience exp) {
        ExperienceJpaEntity e = new ExperienceJpaEntity();
        e.setId(exp.getId()); e.setCompany(exp.getCompany()); e.setCompanyUrl(exp.getCompanyUrl()); e.setRole(exp.getRole());
        e.setDescription(exp.getDescription()); e.setLogoUrl(exp.getLogoUrl());
        e.setStartDate(exp.getStartDate()); e.setEndDate(exp.getEndDate());
        e.setLocation(exp.getLocation()); e.setDisplayOrder(exp.getDisplayOrder()); e.setIsHidden(exp.getIsHidden());
        return e;
    }

    @Override
    public Experience save(Experience exp) {
        ExperienceJpaEntity entity = toEntity(exp);
        if (entity.getId() == null) {
            entity.setId(sequenceService.nextId(SEQUENCE_NAME));
        }
        return toDomain(repo.save(entity));
    }
    @Override public List<Experience> findAll() { return repo.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Experience> findById(Long id) { return repo.findById(id).map(this::toDomain); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
}
