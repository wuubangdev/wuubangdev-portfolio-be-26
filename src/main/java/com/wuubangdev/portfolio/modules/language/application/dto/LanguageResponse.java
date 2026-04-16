package com.wuubangdev.portfolio.modules.language.application.dto;

public record LanguageResponse(
        Long id,
        String code,
        String name,
        Boolean isDefault
) {
}
