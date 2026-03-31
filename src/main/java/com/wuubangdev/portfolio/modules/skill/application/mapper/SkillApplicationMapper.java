package com.wuubangdev.portfolio.modules.skill.application.mapper;

import com.wuubangdev.portfolio.modules.skill.application.dto.SkillRequest;
import com.wuubangdev.portfolio.modules.skill.application.dto.SkillResponse;
import com.wuubangdev.portfolio.modules.skill.domain.model.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillApplicationMapper {

    public Skill toNewDomain(SkillRequest request) {
        return Skill.builder()
                .name(request.name())
                .category(request.category())
                .level(request.level())
                .icon(request.icon())
                .displayOrder(request.displayOrder())
                .build();
    }

    public Skill updateDomain(Skill skill, SkillRequest request) {
        skill.setName(request.name());
        skill.setCategory(request.category());
        skill.setLevel(request.level());
        skill.setIcon(request.icon());
        skill.setDisplayOrder(request.displayOrder());
        return skill;
    }

    public SkillResponse toResponse(Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                skill.getLevel(),
                skill.getIcon(),
                skill.getDisplayOrder()
        );
    }
}
