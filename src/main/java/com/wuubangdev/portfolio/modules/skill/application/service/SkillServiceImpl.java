package com.wuubangdev.portfolio.modules.skill.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.skill.application.mapper.SkillApplicationMapper;
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
    private final SkillApplicationMapper skillApplicationMapper;

    @Override
    @Transactional
    public SkillResponse createSkill(SkillRequest request) {
        Skill skill = skillApplicationMapper.toNewDomain(request);
        return skillApplicationMapper.toResponse(skillRepositoryPort.save(skill));
    }

    @Override
    public List<SkillResponse> getAllSkills() {
        return skillRepositoryPort.findAll().stream().map(skillApplicationMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public SkillResponse updateSkill(Long id, SkillRequest request) {
        Skill skill = skillRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        Skill updatedSkill = skillApplicationMapper.updateDomain(skill, request);
        return skillApplicationMapper.toResponse(skillRepositoryPort.save(updatedSkill));
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        skillRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        skillRepositoryPort.deleteById(id);
    }
}
