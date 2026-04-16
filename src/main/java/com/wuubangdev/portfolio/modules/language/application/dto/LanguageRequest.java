package com.wuubangdev.portfolio.modules.language.application.dto;

public record LanguageRequest(
        String code,
        String name,
        Boolean isDefault
) {
}
