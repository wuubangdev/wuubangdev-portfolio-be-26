package com.wuubangdev.portfolio.modules.post.application.dto;

import java.util.List;

public record PostResponse(
        Long id,
        String title,
        String slug,
        String content,
        String summary,
        String coverImageUrl,
        List<String> tags,
        Boolean published
) {}
