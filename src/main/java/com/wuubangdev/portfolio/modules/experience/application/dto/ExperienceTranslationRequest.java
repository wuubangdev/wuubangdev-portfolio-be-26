package com.wuubangdev.portfolio.modules.experience.application.dto;

public record ExperienceTranslationRequest(
        String locale,
        String company,
        String role,
        String description,
        String location
) {}
