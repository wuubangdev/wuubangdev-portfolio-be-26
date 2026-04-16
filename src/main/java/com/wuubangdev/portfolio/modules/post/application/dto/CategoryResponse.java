package com.wuubangdev.portfolio.modules.post.application.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
