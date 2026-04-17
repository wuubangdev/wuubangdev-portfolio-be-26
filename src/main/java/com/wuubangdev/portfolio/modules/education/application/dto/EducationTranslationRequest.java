package com.wuubangdev.portfolio.modules.education.application.dto;

public record EducationTranslationRequest(
        String locale,
        String institution,
        String degree,
        String fieldOfStudy,
        String description,
        String location
) {
}
