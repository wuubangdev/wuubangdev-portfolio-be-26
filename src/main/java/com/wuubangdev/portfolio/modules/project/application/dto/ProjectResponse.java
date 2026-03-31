package com.wuubangdev.portfolio.modules.project.application.dto;

import java.util.List;

public record ProjectResponse(
        Long id,
        String title,
        String slug,
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
