package com.wuubangdev.portfolio.modules.project.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ProjectTranslationRequest(
        @NotBlank String locale,
        @NotBlank String title,
        String description,
        String content,
        String titleSeo,
        String descriptionSeo,
        List<String> seoKeywords
) {}
