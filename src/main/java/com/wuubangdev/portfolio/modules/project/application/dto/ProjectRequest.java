package com.wuubangdev.portfolio.modules.project.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProjectRequest(
        @NotBlank String title,
        String description,
        List<String> techStack,
        String imageUrl,
        String projectUrl,
        String githubUrl,
        String groupName,
        Boolean featured,
        Integer displayOrder
) {}
