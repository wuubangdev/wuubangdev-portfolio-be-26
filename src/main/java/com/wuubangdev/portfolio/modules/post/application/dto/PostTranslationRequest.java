package com.wuubangdev.portfolio.modules.post.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostTranslationRequest(
        @NotBlank String locale,
        @NotBlank String title,
        String content,
        String summary,
        String titleSeo,
        String descriptionSeo,
        List<String> seoKeywords
) {}
