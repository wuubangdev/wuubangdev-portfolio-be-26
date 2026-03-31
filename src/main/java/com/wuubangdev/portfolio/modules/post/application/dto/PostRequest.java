package com.wuubangdev.portfolio.modules.post.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostRequest(
        @NotBlank String title,
        @NotBlank String slug,
        String category,
        String content,
        String summary,
        String coverImageUrl,
        List<String> tags,
        Boolean published
) {}
