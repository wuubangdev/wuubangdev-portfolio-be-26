package com.wuubangdev.portfolio.modules.skill.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.skill.application.dto.SkillRequest;
import com.wuubangdev.portfolio.modules.skill.application.dto.SkillResponse;
import com.wuubangdev.portfolio.modules.skill.domain.model.Skill;
import com.wuubangdev.portfolio.modules.skill.domain.port.SkillRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepositoryPort skillRepositoryPort;

    @Override
    @Transactional
    public SkillResponse createSkill(SkillRequest request) {
        Skill skill = Skill.builder()
                .name(request.name()).category(request.category()).level(request.level())
                .icon(request.icon()).displayOrder(request.displayOrder()).build();
        return toResponse(skillRepositoryPort.save(skill));
    }

    @Override
    public List<SkillResponse> getAllSkills() {
        return skillRepositoryPort.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public SkillResponse updateSkill(Long id, SkillRequest request) {
        Skill skill = skillRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        skill.setName(request.name()); skill.setCategory(request.category());
        skill.setLevel(request.level()); skill.setIcon(request.icon());
        skill.setDisplayOrder(request.displayOrder());
        return toResponse(skillRepositoryPort.save(skill));
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        skillRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        skillRepositoryPort.deleteById(id);
    }

    private SkillResponse toResponse(Skill s) {
        return new SkillResponse(s.getId(), s.getName(), s.getCategory(), s.getLevel(), s.getIcon(), s.getDisplayOrder());
    }
}
