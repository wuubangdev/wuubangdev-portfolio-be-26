package com.wuubangdev.portfolio.modules.project.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProjectRequest(
        @NotBlank String title,
        @NotBlank String slug,
        String category,
        List<String> tags,
        String description,
        String content,
        List<String> techStack,
        String imageUrl,
        String projectUrl,
        String githubUrl,
        String groupName,
        Boolean featured,
        Integer displayOrder
) {}
