package com.wuubangdev.portfolio.modules.skill.domain.port;

import com.wuubangdev.portfolio.modules.skill.domain.model.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepositoryPort {
    Skill save(Skill skill);
    List<Skill> findAll();
    Optional<Skill> findById(Long id);
    void deleteById(Long id);
}
