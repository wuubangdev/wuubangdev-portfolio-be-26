package com.wuubangdev.portfolio.modules.post.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String slug,
        String category,
        String content,
        String summary,
        String coverImageUrl,
        List<String> tags,
        Boolean published,
        // Thêm mới
        String author,
        String titleSeo,
        String descriptionSeo,
        String thumbnailSeo,
        List<String> seoKeywords,
        String canonicalUrl,
        Boolean indexable,
        Integer likes,
        Integer hearts,
        Integer commentsCount,
        Integer shares,
        String status,
        LocalDateTime createdAt,
        Integer displayOrder,
        Boolean isHidden
) {}
