package com.wuubangdev.portfolio.modules.experience.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.experience.domain.model.Experience;
import com.wuubangdev.portfolio.modules.experience.domain.port.ExperienceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperiencePersistenceAdapter implements ExperienceRepositoryPort {

    private final ExperienceJpaRepository repo;

    private Experience toDomain(ExperienceJpaEntity e) {
        return Experience.builder().id(e.getId()).company(e.getCompany()).role(e.getRole())
                .description(e.getDescription()).logoUrl(e.getLogoUrl()).startDate(e.getStartDate())
                .endDate(e.getEndDate()).location(e.getLocation()).displayOrder(e.getDisplayOrder()).build();
    }

    private ExperienceJpaEntity toEntity(Experience exp) {
        ExperienceJpaEntity e = new ExperienceJpaEntity();
        e.setId(exp.getId()); e.setCompany(exp.getCompany()); e.setRole(exp.getRole());
        e.setDescription(exp.getDescription()); e.setLogoUrl(exp.getLogoUrl());
        e.setStartDate(exp.getStartDate()); e.setEndDate(exp.getEndDate());
        e.setLocation(exp.getLocation()); e.setDisplayOrder(exp.getDisplayOrder());
        return e;
    }

    @Override public Experience save(Experience exp) { return toDomain(repo.save(toEntity(exp))); }
    @Override public List<Experience> findAll() { return repo.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Experience> findById(Long id) { return repo.findById(id).map(this::toDomain); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
}
