package com.wuubangdev.portfolio.modules.project.application.dto;

import java.time.LocalDateTime;
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
        Integer displayOrder,
        String titleSeo,
        String descriptionSeo,
        String thumbnailSeo,
        List<String> seoKeywords,
        String canonicalUrl,
        Boolean indexable,
        LocalDateTime createdAt,
        String locale,
        Boolean translated
) {}
