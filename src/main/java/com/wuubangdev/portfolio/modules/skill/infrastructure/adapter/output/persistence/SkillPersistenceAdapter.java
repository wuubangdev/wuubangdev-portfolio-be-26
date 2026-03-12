package com.wuubangdev.portfolio.modules.skill.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.modules.skill.domain.model.Skill;
import com.wuubangdev.portfolio.modules.skill.domain.port.SkillRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillPersistenceAdapter implements SkillRepositoryPort {

    private final SkillJpaRepository repo;

    private Skill toDomain(SkillJpaEntity e) {
        return Skill.builder().id(e.getId()).name(e.getName()).category(e.getCategory())
                .level(e.getLevel()).icon(e.getIcon()).displayOrder(e.getDisplayOrder()).build();
    }

    private SkillJpaEntity toEntity(Skill s) {
        SkillJpaEntity e = new SkillJpaEntity();
        e.setId(s.getId()); e.setName(s.getName()); e.setCategory(s.getCategory());
        e.setLevel(s.getLevel()); e.setIcon(s.getIcon()); e.setDisplayOrder(s.getDisplayOrder());
        return e;
    }

    @Override public Skill save(Skill skill) { return toDomain(repo.save(toEntity(skill))); }
    @Override public List<Skill> findAll() { return repo.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Skill> findById(Long id) { return repo.findById(id).map(this::toDomain); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }
}
