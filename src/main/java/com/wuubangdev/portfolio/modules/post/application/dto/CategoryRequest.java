package com.wuubangdev.portfolio.modules.post.application.dto;

public record CategoryRequest(
        String name,
        String slug,
        String description
) {
}
