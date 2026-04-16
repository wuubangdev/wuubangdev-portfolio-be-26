package com.wuubangdev.portfolio.modules.skill.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SkillRequest(
        @NotBlank String name,
        @NotBlank String category,
        @Min(1) @Max(100) Integer level,
        String icon,
        Integer displayOrder,
        Boolean isHidden
) {}
