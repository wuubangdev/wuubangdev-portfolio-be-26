package com.wuubangdev.portfolio.modules.skill.application.service;

import com.wuubangdev.portfolio.modules.skill.application.dto.SkillRequest;
import com.wuubangdev.portfolio.modules.skill.application.dto.SkillResponse;

import java.util.List;

public interface SkillService {
    SkillResponse createSkill(SkillRequest request);
    List<SkillResponse> getAllSkills();
    SkillResponse updateSkill(Long id, SkillRequest request);
    void deleteSkill(Long id);
}
