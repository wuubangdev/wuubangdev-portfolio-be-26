package com.wuubangdev.portfolio.modules.profile.application.dto;

public record ProfileTranslationRequest(
        String locale,
        String fullName,
        String title,
        String bio,
        String location
) {
}
